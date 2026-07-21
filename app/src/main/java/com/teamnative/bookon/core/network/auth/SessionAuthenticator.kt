package com.teamnative.bookon.core.network.auth

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/** 401 응답에 대해 refresh token으로 한 번만 갱신한 뒤 원래 요청을 재시도한다. */
@Singleton
class SessionAuthenticator @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val tokenRefreshService: TokenRefreshService,
) : Authenticator {
    private val refreshLock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MaxAuthenticationRetries) return null
        val requestToken = response.request.header(AuthorizationHeader)?.removePrefix(BearerPrefix)
        synchronized(refreshLock) {
            val currentToken = tokenSessionManager.accessToken() ?: return null
            if (requestToken != currentToken) return response.request.newBuilder()
                .header(AuthorizationHeader, "$BearerPrefix$currentToken")
                .build()
            val refreshToken = tokenSessionManager.refreshToken() ?: return null
            val refreshedTokens = tokenRefreshService.refreshBlocking(refreshToken) ?: return null
            return response.request.newBuilder()
                .header(AuthorizationHeader, "$BearerPrefix${refreshedTokens.accessToken}")
                .build()
        }
    }

    private fun responseCount(response: Response): Int {
        var responseCursor: Response? = response
        var count = 1
        while (responseCursor?.priorResponse != null) {
            count += 1
            responseCursor = responseCursor.priorResponse
        }
        return count
    }

    private companion object {
        const val AuthorizationHeader = "Authorization"
        const val BearerPrefix = "Bearer "
        const val MaxAuthenticationRetries = 2
    }
}
