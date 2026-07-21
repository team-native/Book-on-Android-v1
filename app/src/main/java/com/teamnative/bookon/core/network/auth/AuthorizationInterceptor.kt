package com.teamnative.bookon.core.network.auth

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

/** 저장된 access token이 있을 때만 각 요청에 Authorization 헤더를 추가한다. */
@Singleton
class AuthorizationInterceptor @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenSessionManager.accessToken()
        val request = if (accessToken == null) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .header(AuthorizationHeader, "Bearer $accessToken")
                .build()
        }
        return chain.proceed(request)
    }

    private companion object {
        const val AuthorizationHeader = "Authorization"
    }
}
