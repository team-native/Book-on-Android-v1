package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.ResetPasswordUseCase
import com.teamnative.bookon.feature.auth.domain.SendPasswordResetEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val GsmEmailDomain = "@gsm.hs.kr"
private const val PASSWORD_RESET_COUNTDOWN_INTERVAL_MILLIS = 1_000L

@HiltViewModel
class BookOnPasswordResetViewModel @Inject constructor(
    private val sendEmail: SendPasswordResetEmailUseCase,
    private val resetPassword: ResetPasswordUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(PasswordResetFormState())
    private var verificationCountdownJob: Job? = null
    val state: StateFlow<PasswordResetFormState> = mutableState.asStateFlow()

    /** 이메일 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updateEmail(email: String) {
        mutableState.update {
            it.copy(
                email = email,
                error = null,
            )
        }
    }

    /** 인증번호 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updateVerificationCode(code: String) {
        mutableState.update {
            it.copy(
                code = code,
                error = null,
            )
        }
    }

    /** 새 비밀번호 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updatePassword(password: String) {
        mutableState.update {
            it.copy(
                password = password,
                error = null,
            )
        }
    }

    /** 비밀번호 확인 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updatePasswordConfirm(passwordConfirm: String) {
        mutableState.update {
            it.copy(
                confirm = passwordConfirm,
                error = null,
            )
        }
    }

    /** 이메일 화면의 다음 클릭에서 인증번호 발송을 요청하고 성공 시 다음 화면으로 이동한다. */
    fun sendVerificationCode(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val requestEmail = mutableState.value.email.toGsmEmailAddress()
            mutableState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }

            when (val result = sendEmail(requestEmail)) {
                is NetworkResult.Success -> {
                    val passwordResetEmailSession = result.data
                    mutableState.update {
                        it.copy(
                            email = passwordResetEmailSession.email,
                            isLoading = false,
                            error = null,
                        )
                    }
                    startVerificationCountdown(passwordResetEmailSession.expiresInSeconds)
                    onSuccess()
                }

                is NetworkResult.Failure -> fail()
            }
        }
    }

    /** 인증번호 화면의 재전송 클릭에서 발송 요청을 다시 수행한다. */
    fun resendVerificationCode() {
        viewModelScope.launch {
            val requestEmail = mutableState.value.email.toGsmEmailAddress()
            mutableState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }

            when (val result = sendEmail(requestEmail)) {
                is NetworkResult.Success -> {
                    val passwordResetEmailSession = result.data
                    mutableState.update {
                        it.copy(
                            email = passwordResetEmailSession.email,
                            code = "",
                            error = null,
                            isLoading = false,
                        )
                    }
                    startVerificationCountdown(passwordResetEmailSession.expiresInSeconds)
                }

                is NetworkResult.Failure -> fail()
            }
        }
    }

    /** 새 비밀번호 화면의 완료 클릭에서 재설정을 요청하고 성공 시 로그인 화면으로 이동한다. */
    fun resetPassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentState = mutableState.value
            val requestEmail = currentState.email.toGsmEmailAddress()
            mutableState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }

            when (
                resetPassword(
                    requestEmail,
                    currentState.code,
                    currentState.password,
                    currentState.confirm,
                )
            ) {
                is NetworkResult.Success -> {
                    mutableState.update {
                        it.copy(
                            email = requestEmail,
                            isLoading = false,
                        )
                    }
                    onSuccess()
                }

                is NetworkResult.Failure -> fail()
            }
        }
    }

    /** 서버가 반환한 유효 시간부터 인증코드 만료까지 1초 단위로 남은 시간을 갱신한다. */
    private fun startVerificationCountdown(expiresInSeconds: Long) {
        verificationCountdownJob?.cancel()

        val normalizedExpiresInSeconds = expiresInSeconds.coerceAtLeast(0)
        mutableState.update {
            it.copy(verificationRemainingSeconds = normalizedExpiresInSeconds)
        }

        verificationCountdownJob = viewModelScope.launch {
            var remainingSeconds = normalizedExpiresInSeconds

            while (remainingSeconds > 0) {
                delay(PASSWORD_RESET_COUNTDOWN_INTERVAL_MILLIS)
                remainingSeconds -= 1
                mutableState.update {
                    it.copy(verificationRemainingSeconds = remainingSeconds)
                }
            }
        }
    }

    /** 입력값이 아이디 부분이면 학교 이메일 도메인을 추가해 API 요청용 주소를 만든다. */
    private fun String.toGsmEmailAddress(): String {
        val trimmedEmail = trim()
        val emailDomain = trimmedEmail.substringAfterLast('@', missingDelimiterValue = "")

        return when {
            trimmedEmail.isEmpty() -> trimmedEmail
            emailDomain.equals(GsmEmailDomain.removePrefix("@"), ignoreCase = true) -> {
                trimmedEmail.substringBeforeLast('@') + GsmEmailDomain
            }

            '@' in trimmedEmail -> trimmedEmail
            else -> trimmedEmail + GsmEmailDomain
        }
    }

    /** 인증 메일 또는 비밀번호 재설정 요청 실패를 현재 입력값을 유지한 오류 상태로 반영한다. */
    private fun fail() {
        mutableState.update {
            it.copy(
                isLoading = false,
                error = PasswordResetError.RequestFailed,
            )
        }
    }
}

/** 비밀번호 재설정 요청 실패를 UI 리소스로 변환하기 전 표현한다. */
enum class PasswordResetError {
    RequestFailed,
}

data class PasswordResetFormState(
    val email: String = "",
    val code: String = "",
    val verificationRemainingSeconds: Long = 0,
    val password: String = "",
    val confirm: String = "",
    val error: PasswordResetError? = null,
    val isLoading: Boolean = false,
)
