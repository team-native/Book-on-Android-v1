package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.ResetPasswordUseCase
import com.teamnative.bookon.feature.auth.domain.SendPasswordResetEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookOnPasswordResetViewModel @Inject constructor(
    private val sendEmail: SendPasswordResetEmailUseCase,
    private val resetPassword: ResetPasswordUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(PasswordResetFormState())
    val state: StateFlow<PasswordResetFormState> = mutableState.asStateFlow()

    /** 이메일 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updateEmail(email: String) {
        mutableState.value = mutableState.value.copy(
            email = email,
            error = null,
        )
    }

    /** 인증번호 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updateVerificationCode(code: String) {
        mutableState.value = mutableState.value.copy(
            code = code,
            error = null,
        )
    }

    /** 새 비밀번호 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updatePassword(password: String) {
        mutableState.value = mutableState.value.copy(
            password = password,
            error = null,
        )
    }

    /** 비밀번호 확인 입력 이벤트에서 호출되며, 이전 요청 오류를 지운다. */
    fun updatePasswordConfirm(passwordConfirm: String) {
        mutableState.value = mutableState.value.copy(
            confirm = passwordConfirm,
            error = null,
        )
    }

    /** 이메일 화면의 다음 클릭에서 인증번호 발송을 요청하고 성공 시 다음 화면으로 이동한다. */
    fun sendVerificationCode(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val value = mutableState.value
            mutableState.value = value.copy(isLoading = true, error = null)

            when (sendEmail(value.email)) {
                is NetworkResult.Success -> {
                    mutableState.value = value.copy(isLoading = false)
                    onSuccess()
                }

                is NetworkResult.Failure -> fail(value)
            }
        }
    }

    /** 인증번호 화면의 재전송 클릭에서 발송 요청을 다시 수행한다. */
    fun resendVerificationCode() {
        viewModelScope.launch {
            val value = mutableState.value
            mutableState.value = value.copy(isLoading = true, error = null)

            when (sendEmail(value.email)) {
                is NetworkResult.Success -> {
                    mutableState.value = value.copy(
                        code = "",
                        error = null,
                        isLoading = false,
                    )
                }

                is NetworkResult.Failure -> fail(value)
            }
        }
    }

    /** 새 비밀번호 화면의 완료 클릭에서 재설정을 요청하고 성공 시 로그인 화면으로 이동한다. */
    fun resetPassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val value = mutableState.value
            mutableState.value = value.copy(isLoading = true, error = null)

            when (resetPassword(value.email, value.code, value.password, value.confirm)) {
                is NetworkResult.Success -> {
                    mutableState.value = value.copy(isLoading = false)
                    onSuccess()
                }

                is NetworkResult.Failure -> fail(value)
            }
        }
    }

    private fun fail(value: PasswordResetFormState) {
        mutableState.value = value.copy(
            isLoading = false,
            error = PasswordResetError.RequestFailed,
        )
    }
}

/** 비밀번호 재설정 요청 실패를 UI 리소스로 변환하기 전 표현한다. */
enum class PasswordResetError {
    RequestFailed,
}

data class PasswordResetFormState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val confirm: String = "",
    val error: PasswordResetError? = null,
    val isLoading: Boolean = false,
)
