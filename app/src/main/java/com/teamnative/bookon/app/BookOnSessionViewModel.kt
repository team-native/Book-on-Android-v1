package com.teamnative.bookon.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.auth.TokenRefreshService
import com.teamnative.bookon.core.network.auth.TokenRefreshResult
import com.teamnative.bookon.core.network.auth.TokenSessionManager
import com.teamnative.bookon.feature.auth.domain.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

sealed interface BookOnSessionUiState {
    data object Checking : BookOnSessionUiState
    data object Authenticated : BookOnSessionUiState
    data object Unauthenticated : BookOnSessionUiState
    data object RetryableError : BookOnSessionUiState
}

/** 앱 시작 시 저장된 refresh token으로 세션을 검증해 첫 화면을 결정한다. */
@HiltViewModel
class BookOnSessionViewModel @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val tokenRefreshService: TokenRefreshService,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow<BookOnSessionUiState>(BookOnSessionUiState.Checking)
    val uiState: StateFlow<BookOnSessionUiState> = mutableUiState.asStateFlow()

    private var restored = false

    init {
        observeSession()
        restoreSession()
    }

    /** 앱 실행 직후 암호화 토큰을 복원하고 refresh 성공 여부를 화면 상태로 반영한다. */
    private fun restoreSession() {
        viewModelScope.launch {
            val savedTokens = tokenSessionManager.restore()
            restored = true
            val nextState = if (savedTokens == null) {
                BookOnSessionUiState.Unauthenticated
            } else {
                when (tokenRefreshService.refresh(savedTokens.refreshToken)) {
                    is TokenRefreshResult.Success -> BookOnSessionUiState.Authenticated
                    TokenRefreshResult.InvalidToken -> BookOnSessionUiState.Unauthenticated
                    is TokenRefreshResult.RetryableFailure -> BookOnSessionUiState.RetryableError
                }
            }
            mutableUiState.value = nextState
        }
    }

    /** 토큰 저장·삭제 후 앱 전체 인증 그래프를 즉시 전환한다. */
    private fun observeSession() {
        viewModelScope.launch {
            tokenSessionManager.tokens.collectLatest { tokens ->
                if (restored && tokens != null) mutableUiState.value = BookOnSessionUiState.Authenticated
                if (restored && tokens == null && mutableUiState.value != BookOnSessionUiState.RetryableError) {
                    mutableUiState.value = BookOnSessionUiState.Unauthenticated
                }
            }
        }
    }

    /** 자동 로그인 확인 실패 화면에서 refresh 검증을 다시 요청한다. */
    fun retryAutoLogin() = restoreSession()

    /** 로그아웃 시 서버 세션 폐기를 요청한 뒤, 성공 여부와 관계없이 이 기기의 토큰을 제거한다. */
    fun logout() {
        viewModelScope.launch {
            val refreshToken = tokenSessionManager.refreshToken()
            tokenSessionManager.clear()
            mutableUiState.value = BookOnSessionUiState.Unauthenticated
            if (refreshToken != null) logoutUseCase(refreshToken)
        }
    }
}
