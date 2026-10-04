package com.teamnative.bookon.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.auth.TokenRefreshService
import com.teamnative.bookon.core.network.auth.TokenRefreshResult
import com.teamnative.bookon.core.network.auth.TokenSessionManager
import com.teamnative.bookon.core.network.auth.SessionSnapshot
import com.teamnative.bookon.core.network.auth.SessionStorageException
import com.teamnative.bookon.core.notification.FcmTokenRegistrationWorker
import com.teamnative.bookon.feature.auth.domain.LogoutUseCase
import com.teamnative.bookon.feature.fcm.domain.ClearFcmTokenOnLogoutUseCase
import com.teamnative.bookon.feature.fcm.domain.SyncFcmTokenOnAuthenticationUseCase
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withTimeoutOrNull

sealed interface BookOnSessionUiState {
    data object Checking : BookOnSessionUiState
    data class Authenticated(val epoch: Long) : BookOnSessionUiState
    data object Unauthenticated : BookOnSessionUiState
    data class StorageError(val isLogout: Boolean) : BookOnSessionUiState
    data object RetryableError : BookOnSessionUiState
}

@HiltViewModel
class BookOnSessionViewModel @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val tokenRefreshService: TokenRefreshService,
    private val logoutUseCase: LogoutUseCase,
    private val syncFcmTokenOnAuthenticationUseCase: SyncFcmTokenOnAuthenticationUseCase,
    private val clearFcmTokenOnLogoutUseCase: ClearFcmTokenOnLogoutUseCase,
    @ApplicationContext private val applicationContext: Context,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow<BookOnSessionUiState>(BookOnSessionUiState.Checking)
    val uiState: StateFlow<BookOnSessionUiState> = mutableUiState.asStateFlow()
    private var restored = false
    private var validationJob: Job? = null
    private var validatingEpoch: Long? = null

    init {
        observeSession()
        validationJob = viewModelScope.launch {
            try {
                tokenSessionManager.restore()
                restored = true
                validate(tokenSessionManager.snapshot.value)
            } catch (exception: SessionStorageException) {
                mutableUiState.value = BookOnSessionUiState.StorageError(isLogout = false)
            }
        }
    }

    private suspend fun validate(expected: SessionSnapshot) {
        if (expected.tokens == null) {
            mutableUiState.value = BookOnSessionUiState.Unauthenticated
            return
        }
        validatingEpoch = expected.epoch
        mutableUiState.value = BookOnSessionUiState.Checking
        val refreshResult = tokenRefreshService.refresh(expected)
        validatingEpoch = null
        val current = tokenSessionManager.snapshot.value
        val isCurrentExpiration = refreshResult == TokenRefreshResult.InvalidToken &&
        current.epoch == expected.epoch + 1L && current.tokens == null
        if (current.epoch != expected.epoch && !isCurrentExpiration) {
            return
        }
        mutableUiState.value = when (refreshResult) {
            is TokenRefreshResult.Success -> BookOnSessionUiState.Authenticated(expected.epoch)
            TokenRefreshResult.InvalidToken -> BookOnSessionUiState.Unauthenticated
            is TokenRefreshResult.RetryableFailure -> BookOnSessionUiState.RetryableError
            TokenRefreshResult.StorageFailure -> BookOnSessionUiState.StorageError(isLogout = false)
            TokenRefreshResult.Superseded -> return
        }
        if (refreshResult is TokenRefreshResult.Success) {
            syncFcmTokenOnAuthenticationUseCase()
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            tokenSessionManager.snapshot.collectLatest { snapshot ->

                if (!restored || validatingEpoch == snapshot.epoch) {
                    return@collectLatest
                }
                if (snapshot.tokens == null) {
                    FcmTokenRegistrationWorker.cancel(applicationContext)
                    mutableUiState.value = BookOnSessionUiState.Unauthenticated
                } else {
                    mutableUiState.value = BookOnSessionUiState.Authenticated(snapshot.epoch)
                    syncFcmTokenOnAuthenticationUseCase()
                }
            }
        }
    }

    fun retryAutoLogin() {
        if (validationJob?.isActive == true) {
            return
        }
        validationJob = viewModelScope.launch {
            try {
                if (!restored) {
                    tokenSessionManager.restore()
                    restored = true
                }
                validate(tokenSessionManager.snapshot.value)
            } catch (exception: SessionStorageException) {
                mutableUiState.value = BookOnSessionUiState.StorageError(isLogout = false)
            }
        }
    }

    /** 로컬 세션을 즉시 종료하고 이전 세션의 서버 정리를 제한된 시간에 수행한다. */
    fun logout() {
        validationJob?.cancel()
        validatingEpoch = null
        viewModelScope.launch {
            val cleanup = try {
                tokenSessionManager.beginLogout()
            } catch (exception: SessionStorageException) {
                mutableUiState.value = BookOnSessionUiState.StorageError(isLogout = true)
                return@launch
            }
            FcmTokenRegistrationWorker.cancel(applicationContext)
            mutableUiState.value = BookOnSessionUiState.Unauthenticated
            try {
                withTimeoutOrNull(CleanupTotalTimeoutMillis) {
                    cleanup.previousCleanup?.await()
                    withTimeoutOrNull(CleanupRequestTimeoutMillis) {
                        clearFcmTokenOnLogoutUseCase(cleanup)
                    }
                    withTimeoutOrNull(CleanupRequestTimeoutMillis) {
                        logoutUseCase(cleanup)
                    }
                }
            } finally {
                cleanup.dispose()
                tokenSessionManager.finishCleanup(cleanup)
            }
        }
    }
}

private const val CleanupTotalTimeoutMillis = 10000L
private const val CleanupRequestTimeoutMillis = 5000L
