package com.teamnative.bookon.core.network.auth

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/** Authenticator가 동기 제약 안에서 refresh API를 한 번 호출하고 새 토큰을 저장하도록 돕는다. */
@Singleton
class TokenRefreshService @Inject constructor(
    private val tokenRefreshApiService: TokenRefreshApiService,
    private val apiExecutor: ApiExecutor,
    private val tokenSessionManager: TokenSessionManager,
) {
    /** 앱 시작과 Authenticator에서 refresh token을 새 토큰 쌍으로 교체한다. */
    suspend fun refresh(refreshToken: String): AuthTokens? {
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
                refreshedTokens
            }
            is NetworkResult.Failure -> {
                tokenSessionManager.clear()
                null
            }
        }
    }

    /** OkHttp Authenticator의 동기 callback에서만 suspend refresh 호출을 연결한다. */
    fun refreshBlocking(refreshToken: String): AuthTokens? = runBlocking(Dispatchers.IO) {
        refresh(refreshToken)
    }
}
