package com.teamnative.bookon.core.network.auth

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

sealed interface TokenRefreshResult {
    data class Success(val tokens: AuthTokens) : TokenRefreshResult
    data object InvalidToken : TokenRefreshResult
    data object StorageFailure : TokenRefreshResult
    data object Superseded : TokenRefreshResult
    data class RetryableFailure(val error: NetworkError) : TokenRefreshResult
}

@Singleton
class TokenRefreshService @Inject constructor(
    private val tokenRefreshApiService: TokenRefreshApiService,
    private val apiExecutor: ApiExecutor,
    private val tokenSessionManager: TokenSessionManager,
) {
    private val refreshMutex = Mutex()

    /** 동일 세션에서만 갱신 응답을 저장하며 교체된 세션에는 영향을 주지 않는다. */
    suspend fun refresh(expected: SessionSnapshot): TokenRefreshResult = refreshMutex.withLock {
        val current = tokenSessionManager.snapshot.value
        if (current.epoch != expected.epoch || current.tokens == null) {
            return@withLock TokenRefreshResult.Superseded
        }
        if (current.tokens != expected.tokens) {
            return@withLock TokenRefreshResult.Success(current.tokens)
        }
        try {
            when (val refreshResult = apiExecutor.execute {
                    tokenRefreshApiService.refresh(RefreshTokenRequestDto(current.tokens.refreshToken))
            }) {
                is NetworkResult.Success -> {
                    val refreshedTokens = AuthTokens(
                        refreshResult.data.accessToken,
                        refreshResult.data.refreshToken
                    )
                    if (tokenSessionManager.saveIfCurrent(
                            expected,
                            refreshedTokens
                    )) {
                        TokenRefreshResult.Success(refreshedTokens)
                    } else {
                        TokenRefreshResult.Superseded
                    }
                }
                is NetworkResult.Failure -> {
                    if (!tokenSessionManager.isCurrent(expected)) {
                        TokenRefreshResult.Superseded
                    } else if (refreshResult.error.isInvalidRefreshToken()) {
                        if (tokenSessionManager.clearIfCurrent(expected)) {
                            TokenRefreshResult.InvalidToken
                        } else {
                            TokenRefreshResult.Superseded
                        }
                    } else {
                        TokenRefreshResult.RetryableFailure(refreshResult.error)
                    }
                }
            }
        } catch (exception: SessionStorageException) {
            TokenRefreshResult.StorageFailure
        }
    }

    fun expireBlocking(expected: SessionSnapshot) = runBlocking(Dispatchers.IO) {
        try {
            tokenSessionManager.clearIfCurrent(expected)
        } catch (exception: SessionStorageException) {
            false
        }
    }

    fun refreshBlocking(expected: SessionSnapshot): AuthTokens? = runBlocking(Dispatchers.IO) {
        (refresh(expected) as? TokenRefreshResult.Success)?.tokens
    }
}

private fun NetworkError.isInvalidRefreshToken(): Boolean =
this is NetworkError.Http && statusCode == 401 && errorCode == 4010
