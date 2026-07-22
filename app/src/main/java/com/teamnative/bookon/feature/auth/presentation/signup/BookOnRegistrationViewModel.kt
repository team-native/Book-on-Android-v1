package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
)

@HiltViewModel
class BookOnRegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val verifyRegistrationUseCase: VerifyRegistrationUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(BookOnRegistrationState())
    val state: StateFlow<BookOnRegistrationState> = mutableState.asStateFlow()

    fun update(transform: (BookOnRegistrationState) -> BookOnRegistrationState) {
        mutableState.value = transform(mutableState.value).copy(errorMessage = null)
    }

    /**
     * 비밀번호 단계의 다음 버튼에서 메일 발송과 인증 세션 생성을 요청한다.
     * 진행 중인 요청은 무시하며, 성공·실패 시 로딩 상태를 해제한다.
     */
    fun requestVerification(onSuccess: () -> Unit) = viewModelScope.launch {
        if (mutableState.value.isLoading) return@launch

        val value = mutableState.value
        val department = value.department ?: return@launch
        val gender = value.gender ?: return@launch
        mutableState.value = value.copy(isLoading = true, errorMessage = null)
        when (val result = registerUseCase(RegistrationDraft(value.email, value.name, department.name, gender.name, value.password, value.passwordConfirm))) {
            is NetworkResult.Success -> {
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    sessionId = result.data.sessionId,
                )
                onSuccess()
            }
            is NetworkResult.Failure -> mutableState.value = mutableState.value.copy(isLoading = false, errorMessage = result.error.message())
        }
    }

    /** 인증 코드 단계의 확인 버튼에서 서버 가입 완료를 검증한다. */
    fun verify(passcode: String, onSuccess: () -> Unit) = viewModelScope.launch {
        val sessionId = mutableState.value.sessionId ?: return@launch
        mutableState.value = mutableState.value.copy(isLoading = true, errorMessage = null)
        when (val result = verifyRegistrationUseCase(sessionId, passcode)) {
            is NetworkResult.Success -> {
                mutableState.value = mutableState.value.copy(isLoading = false)
                onSuccess()
            }
            is NetworkResult.Failure -> mutableState.value = mutableState.value.copy(isLoading = false, errorMessage = result.error.message())
        }
    }

    /** 인증번호 재전송 클릭 시 현재 가입 입력값으로 새 인증 세션을 요청한다. */
    fun resendVerification() = viewModelScope.launch {
        val value = mutableState.value
        val department = value.department ?: return@launch
        val gender = value.gender ?: return@launch
        mutableState.value = value.copy(isLoading = true, errorMessage = null)
        when (val result = registerUseCase(
            RegistrationDraft(value.email, value.name, department.name, gender.name, value.password, value.passwordConfirm),
        )) {
            is NetworkResult.Success -> mutableState.value = mutableState.value.copy(
                isLoading = false,
                sessionId = result.data.sessionId,
            )
            is NetworkResult.Failure -> mutableState.value = mutableState.value.copy(
                isLoading = false,
                errorMessage = result.error.message(),
            )
        }
    }
}

private fun com.teamnative.bookon.core.network.NetworkError.message(): String = when (this) {
    is com.teamnative.bookon.core.network.NetworkError.Http -> message
    else -> "네트워크 연결을 확인한 뒤 다시 시도해 주세요."
}
