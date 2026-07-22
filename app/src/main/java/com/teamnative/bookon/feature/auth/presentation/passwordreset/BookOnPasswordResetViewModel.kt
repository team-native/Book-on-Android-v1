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
    fun update(transform: (PasswordResetFormState) -> PasswordResetFormState) {
        mutableState.value = transform(mutableState.value).copy(error = null)
    }
    fun continueStep(onComplete: () -> Unit) = viewModelScope.launch {
        val value = mutableState.value
        mutableState.value = value.copy(isLoading = true, error = null)
        when (value.step) {
            BookOnPasswordResetStep.Email -> when (sendEmail(value.email)) {
                is NetworkResult.Success -> {
                    mutableState.value = value.copy(
                        step = BookOnPasswordResetStep.Verification,
                    )
                }

                is NetworkResult.Failure -> fail(value)
            }
            BookOnPasswordResetStep.Verification -> mutableState.value = value.copy(step = BookOnPasswordResetStep.Password)
            BookOnPasswordResetStep.Password -> when (
                resetPassword(value.email, value.code, value.password, value.confirm)
            ) {
                is NetworkResult.Success -> {
                    mutableState.value = value.copy(isLoading = false)
                    onComplete()
                }

                is NetworkResult.Failure -> fail(value)
            }
        }
    }
    fun resend() = viewModelScope.launch {
        val value = mutableState.value
        when (sendEmail(value.email)) {
            is NetworkResult.Success -> mutableState.value = value.copy(code = "", error = null, isLoading = false)
            is NetworkResult.Failure -> fail(value)
        }
    }
    private fun fail(value: PasswordResetFormState) {
        mutableState.value = value.copy(
            isLoading = false,
            error = "요청에 실패했습니다. 다시 시도해 주세요.",
        )
    }
}
data class PasswordResetFormState(
    val step: BookOnPasswordResetStep = BookOnPasswordResetStep.Email,
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val confirm: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
)
