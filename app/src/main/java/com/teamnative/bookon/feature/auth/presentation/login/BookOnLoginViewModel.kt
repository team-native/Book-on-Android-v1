package com.teamnative.bookon.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.network.auth.AuthTokens
import com.teamnative.bookon.core.network.auth.TokenSessionManager
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed interface BookOnLoginEffect {
    data object NavigateToHome : BookOnLoginEffect
}

@HiltViewModel
class BookOnLoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenSessionManager: TokenSessionManager,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        BookOnLoginUiState(
            title = "",
            email = BookOnTextFieldUiModel(value = ""),
            password = BookOnPasswordFieldUiModel(value = ""),
        ),
    )
    val uiState: StateFlow<BookOnLoginUiState> = mutableUiState.asStateFlow()

    private val mutableEffect = MutableSharedFlow<BookOnLoginEffect>()
    val effect = mutableEffect.asSharedFlow()

    /** 이메일 변경 시 로그인 실패 문구를 해제한다. */
    fun updateEmail(email: String) {
        val currentState = mutableUiState.value
        mutableUiState.value = currentState.copy(
            email = currentState.email.copy(value = email),
            password = currentState.password.copy(errorText = null),
            hasMissingCredentials = false,
            loginError = null,
        )
    }

    /** 비밀번호 변경 시 로그인 실패 문구를 해제한다. */
    fun updatePassword(password: String) {
        val currentState = mutableUiState.value
        mutableUiState.value = currentState.copy(
            password = currentState.password.copy(value = password, errorText = null),
            hasMissingCredentials = false,
            loginError = null,
        )
    }

    /** 로그인 버튼 클릭에서 빈 입력을 검사하고, 성공 시 이동 효과를 발행한다. */
    fun login() = viewModelScope.launch {
        val state = mutableUiState.value
        if (state.isSubmitting) {
            return@launch
        }

        if (state.email.value.isBlank() || state.password.value.isBlank()) {
            mutableUiState.value = state.copy(hasMissingCredentials = true)
            return@launch
        }

        mutableUiState.value = state.copy(isSubmitting = true)
        val loginId = state.email.value.takeIf { it.contains('@') }
            ?: "${state.email.value}@gsm.hs.kr"

        when (val result = loginUseCase(loginId, state.password.value)) {
            is NetworkResult.Success -> {
                tokenSessionManager.save(
                    AuthTokens(
                        result.data.accessToken,
                        result.data.refreshToken,
                    ),
                )
                mutableUiState.value = mutableUiState.value.copy(isSubmitting = false)
                mutableEffect.emit(BookOnLoginEffect.NavigateToHome)
            }
            is NetworkResult.Failure -> {
                val currentState = mutableUiState.value
                mutableUiState.value = currentState.copy(
                    isSubmitting = false,
                    loginError = BookOnLoginError.RequestFailed,
                )
            }
        }
    }
}
