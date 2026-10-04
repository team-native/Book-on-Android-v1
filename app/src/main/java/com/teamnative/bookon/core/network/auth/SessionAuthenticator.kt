package com.teamnative.bookon.core.network.auth

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.SerializationException
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
    private val json: Json,
) : Authenticator {
    private val refreshLock = Any()

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        if (response.request.tag(SessionCleanupAuthorization::class.java) != null) {
            return null
        }
        if (!isBookOnTokenFailure(response)) {
            return null
        }
        val requestSession = response.request.tag(SessionSnapshot::class.java) ?: return null
        if (responseCount(response) >= MaxAuthenticationRetries) {
            tokenRefreshService.expireBlocking(requestSession)
            return null
        }
        val requestToken = response.request.header(AuthorizationHeader)?.removePrefix(BearerPrefix)
        synchronized(refreshLock) {
            val currentSession = tokenSessionManager.snapshot.value
            if (requestSession.epoch != currentSession.epoch) {
                return null
            }
            val currentToken = tokenSessionManager.accessToken() ?: return null
            if (requestToken != currentToken) return response.request.newBuilder()
            .tag(
                SessionSnapshot::class.java,
                currentSession
            )
            .header(
                AuthorizationHeader,
                "$BearerPrefix$currentToken"
            )
            .build()
            val refreshedTokens = tokenRefreshService.refreshBlocking(currentSession) ?: return null
            if (currentSession.epoch != tokenSessionManager.snapshot.value.epoch) {
                return null
            }
            return response.request.newBuilder()
            .tag(
                SessionSnapshot::class.java,
                SessionSnapshot(
                    currentSession.epoch,
                    refreshedTokens
                )
            )
            .header(
                AuthorizationHeader,
                "$BearerPrefix${refreshedTokens.accessToken}"
            )
            .build()
        }
    }

    private fun isBookOnTokenFailure(response: Response): Boolean {
        if (response.code != 401) {
            return false
        }
        return try {
            json.parseToJsonElement(response.peekBody(65536L).string())
            .jsonObject["errorCode"]?.jsonPrimitive?.intOrNull == 4010
        } catch (exception: SerializationException) {
            false
        } catch (exception: IllegalArgumentException) {
            false
        } catch (exception: java.io.IOException) {
            false
        }
    }

    private fun responseCount(response: Response): Int {
        var responseCursor: Response? = response
        var count = 0
        while (responseCursor != null) {
            if (responseCursor.code == 401) {
                count += 1
            }
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
