package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.RegisterUseCase
import com.teamnative.bookon.feature.auth.domain.RegistrationDraft
import com.teamnative.bookon.feature.auth.domain.VerifyRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.time.Clock
import java.time.Instant
import java.time.format.DateTimeParseException

data class BookOnRegistrationState(
    val email: String = "",
    val name: String = "",
    val gender: BookOnGender? = null,
    val department: BookOnDepartment? = null,
    val password: String = "",
    val passwordConfirm: String = "",
    val privacyAccepted: Boolean = false,
    val sessionId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailError: BookOnRegistrationEmailError? = null,
    val hasMissingRegistrationFields: Boolean = false,
    val hasInvalidDeadline: Boolean = false,
    val verificationEmail: String = "",
    val verificationCode: String = "",
    val expiresAtMillis: Long? = null,
    val remainingSeconds: Long = 0,

)

/** 회원가입 이메일 입력 필드에 표시할 오류 종류다. */
enum class BookOnRegistrationEmailError {
    AlreadyUsed,
}

@HiltViewModel
class BookOnRegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val verifyRegistrationUseCase: VerifyRegistrationUseCase,
    private val clock: Clock = Clock.systemUTC(),
) : ViewModel() {
    private val mutableState = MutableStateFlow(BookOnRegistrationState())
    val state: StateFlow<BookOnRegistrationState> = mutableState.asStateFlow()
    private var countdownJob: Job? = null
    private var requestJob: Job? = null
    private var requestGeneration = 0L

    /** 회원가입 입력값을 갱신하고 이전 서버 오류를 제거한다. */
    fun update(transform: (BookOnRegistrationState) -> BookOnRegistrationState) {
        if (mutableState.value.isLoading) {
            return
        }
        mutableState.value = transform(mutableState.value).copy(
            errorMessage = null,
            emailError = null,
            hasMissingRegistrationFields = false,
        )
    }

    // 최종 가입 요청에서 입력 누락과 중복 제출을 검사한다.
    fun requestVerification(
        onSuccess: () -> Unit,
        onEmailAlreadyUsed: () -> Unit
    ) {
        requestSession(
            onSuccess,
            onEmailAlreadyUsed
        )
    }

    private fun requestSession(
        onSuccess: () -> Unit,
        onEmailAlreadyUsed: (() -> Unit)?
    ) {
        val current = mutableState.value
        if (current.isLoading) {
            return
        }
        val department = current.department
        val gender = current.gender
        if (current.email.isBlank() || current.name.isBlank() || department == null || gender == null) {
            mutableState.value = current.copy(hasMissingRegistrationFields = true)
            return
        }
        if (!current.privacyAccepted ||
            !com.teamnative.bookon.feature.auth.presentation.component.BookOnPasswordPolicy.isValid(current.password) ||
            current.password != current.passwordConfirm
        ) {
            return
        }
        val draft = RegistrationDraft(
            current.email,
            current.name,
            department.name,
            gender.name,
            current.password,
            current.passwordConfirm,
        )
        mutableState.value = current.copy(
            isLoading = true,
            errorMessage = null,
            emailError = null
        )
        val generation = ++requestGeneration
        requestJob = viewModelScope.launch {
            val registrationResult = registerUseCase(draft)
            if (generation != requestGeneration) {
                return@launch
            }
            when (registrationResult) {
                is NetworkResult.Success -> {
                    val deadline = try {
                        Instant.parse(registrationResult.data.expiresAt).toEpochMilli()
                    } catch (exception: DateTimeParseException) {
                        null
                    } catch (exception: ArithmeticException) {
                        null
                    }
                    mutableState.value = mutableState.value.copy(
                        isLoading = false,
                        sessionId = if (deadline == null) null else registrationResult.data.sessionId,
                        verificationEmail = registrationResult.data.email,
                        verificationCode = "",
                        expiresAtMillis = deadline,
                        hasInvalidDeadline = deadline == null,
                    )
                    updateRemainingTime()
                    startCountdown()
                    if (deadline != null) {
                        onSuccess()
                    }
                }
                is NetworkResult.Failure -> {
                    if (registrationResult.error.isEmailAlreadyUsed() && onEmailAlreadyUsed != null) {
                        mutableState.value = mutableState.value.copy(
                            isLoading = false,
                            emailError = BookOnRegistrationEmailError.AlreadyUsed,
                        )
                        onEmailAlreadyUsed()
                    } else {
                        mutableState.value = mutableState.value.copy(
                            isLoading = false,
                            errorMessage = registrationResult.error.message(),
                        )
                    }
                }
            }
        }
    }

    fun updateCode(code: String) {
        if (mutableState.value.isLoading) {
            return
        }
        mutableState.value = mutableState.value.copy(
            verificationCode = code.filter(Char::isDigit).take(6),
            errorMessage = null,
        )
    }

    // 인증과 재전송을 직렬화해 이번 세션의 코드만 검증한다.
    fun verify(
        passcode: String,
        onSuccess: () -> Unit
    ) {
        updateRemainingTime()
        val current = mutableState.value
        val sessionId = current.sessionId ?: return
        if (current.isLoading || current.remainingSeconds <= 0 || passcode.length != 6 || !passcode.all(Char::isDigit)) {
            return
        }
        mutableState.value = current.copy(
            isLoading = true,
            errorMessage = null
        )
        val generation = ++requestGeneration
        requestJob = viewModelScope.launch {
            val verificationResult = verifyRegistrationUseCase(
                sessionId,
                passcode
            )
            if (generation != requestGeneration) {
                return@launch
            }
            when (verificationResult) {
                is NetworkResult.Success -> {
                    countdownJob?.cancel()
                    mutableState.value = mutableState.value.copy(isLoading = false)
                    onSuccess()
                }
                is NetworkResult.Failure -> {
                    mutableState.value = mutableState.value.copy(
                        isLoading = false,
                        errorMessage = verificationResult.error.message(),
                    )
                }
            }
        }
    }

    fun resendVerification() {
        requestSession(
            onSuccess = {
            },
            onEmailAlreadyUsed = null
        )
    }

    fun cancelPendingRequest() {
        requestGeneration += 1L
        requestJob?.cancel()
        mutableState.value = mutableState.value.copy(isLoading = false)
    }

    fun updateRemainingTime() {
        val deadline = mutableState.value.expiresAtMillis
        val remainingMillis = if (deadline == null) 0L else (deadline - clock.millis()).coerceAtLeast(0L)
        mutableState.value = mutableState.value.copy(remainingSeconds = (remainingMillis + 999L) / 1000L)
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (mutableState.value.remainingSeconds > 0) {
                delay(1000L)
                updateRemainingTime()
            }
        }
    }

}

/** 서버가 이메일 중복을 나타내는 HTTP 오류를 반환했는지 확인한다. */
private fun NetworkError.isEmailAlreadyUsed(): Boolean = when (this) {
    is NetworkError.Http -> {
        statusCode == java.net.HttpURLConnection.HTTP_CONFLICT ||
        message.contains("이미 사용 중인 이메일") ||
        message.contains(
            "already used",
            ignoreCase = true
        ) ||
        message.contains(
            "email already",
            ignoreCase = true
        )
    }

    else -> false
}

private fun com.teamnative.bookon.core.network.NetworkError.message(): String = when (this) {
    is NetworkError.Http -> message
    else -> "네트워크 연결을 확인한 뒤 다시 시도해 주세요."
}
