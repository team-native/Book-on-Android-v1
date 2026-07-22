package com.teamnative.bookon.core.network.auth

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/** Authenticator가 동기 제약 안에서 refresh API를 한 번 호출하고 새 토큰을 저장하도록 돕는다. */
sealed interface TokenRefreshResult {
    data class Success(val tokens: AuthTokens) : TokenRefreshResult
    data object InvalidToken : TokenRefreshResult
    data class RetryableFailure(val error: com.teamnative.bookon.core.network.NetworkError) : TokenRefreshResult
}

@Singleton
class TokenRefreshService @Inject constructor(
    private val tokenRefreshApiService: TokenRefreshApiService,
    private val apiExecutor: ApiExecutor,
    private val tokenSessionManager: TokenSessionManager,
) {
    /** 앱 시작과 Authenticator에서 refresh token을 새 토큰 쌍으로 교체한다. */
    suspend fun refresh(refreshToken: String): TokenRefreshResult {
        return when (
            val result = apiExecutor.execute {
                tokenRefreshApiService.refresh(RefreshTokenRequestDto(refreshToken))
            }
        ) {
            is NetworkResult.Success -> {
                val refreshedTokens = AuthTokens(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                )
                tokenSessionManager.save(refreshedTokens)
                TokenRefreshResult.Success(refreshedTokens)
            }
            is NetworkResult.Failure -> {
                if (result.error.isInvalidRefreshToken()) {
                    tokenSessionManager.clear()
                    TokenRefreshResult.InvalidToken
                } else {
                    TokenRefreshResult.RetryableFailure(result.error)
                }
            }
        }
    }

    /** OkHttp Authenticator의 동기 callback에서만 suspend refresh 호출을 연결한다. */
    fun refreshBlocking(refreshToken: String): AuthTokens? = runBlocking(Dispatchers.IO) {
        (refresh(refreshToken) as? TokenRefreshResult.Success)?.tokens
    }
}

private fun com.teamnative.bookon.core.network.NetworkError.isInvalidRefreshToken(): Boolean =
    this is com.teamnative.bookon.core.network.NetworkError.Http &&
        (statusCode == UnauthorizedStatus || errorCode == InvalidTokenErrorCode)

private const val UnauthorizedStatus = 401
private const val InvalidTokenErrorCode = 4010
