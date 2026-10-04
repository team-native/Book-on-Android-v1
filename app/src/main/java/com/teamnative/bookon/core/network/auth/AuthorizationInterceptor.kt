package com.teamnative.bookon.core.network.auth

import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class AuthorizationInterceptor @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val cleanup = original.tag(SessionCleanupAuthorization::class.java)
        val snapshot = original.tag(SessionSnapshot::class.java) ?: tokenSessionManager.snapshot.value
        val accessToken = if (cleanup != null) {
            cleanup.accessToken() ?: throw IOException("Session cleanup credentials expired")
        } else {
            if (snapshot.epoch != tokenSessionManager.snapshot.value.epoch) {
                throw IOException("Request session was replaced")
            }
            snapshot.tokens?.accessToken
        }
        val builder = original.newBuilder().tag(SessionSnapshot::class.java, snapshot)
        if (accessToken != null) {
            builder.header("Authorization", "Bearer $accessToken")
        }
        return chain.proceed(builder.build())
    }
}
