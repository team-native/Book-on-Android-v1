package com.teamnative.bookon.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.auth.TokenRefreshService
import com.teamnative.bookon.core.network.auth.TokenSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BookOnSessionUiState {
    data object Checking : BookOnSessionUiState
    data object Authenticated : BookOnSessionUiState
    data object Unauthenticated : BookOnSessionUiState
}

/** 앱 시작 시 저장된 refresh token으로 세션을 검증해 첫 화면을 결정한다. */
@HiltViewModel
class BookOnSessionViewModel @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val tokenRefreshService: TokenRefreshService,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow<BookOnSessionUiState>(BookOnSessionUiState.Checking)
    val uiState: StateFlow<BookOnSessionUiState> = mutableUiState.asStateFlow()

    init {
        restoreSession()
    }

    /** 앱 실행 직후 암호화 토큰을 복원하고 refresh 성공 여부를 화면 상태로 반영한다. */
    private fun restoreSession() {
        viewModelScope.launch {
            val savedTokens = tokenSessionManager.restore()
            mutableUiState.value = if (savedTokens == null) {
                BookOnSessionUiState.Unauthenticated
            } else if (tokenRefreshService.refresh(savedTokens.refreshToken) != null) {
                BookOnSessionUiState.Authenticated
            } else {
                BookOnSessionUiState.Unauthenticated
            }
        }
    }
}
