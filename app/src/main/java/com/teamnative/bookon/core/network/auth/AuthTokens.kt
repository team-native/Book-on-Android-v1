package com.teamnative.bookon.core.network.auth

/** 인증 Interceptor와 Authenticator가 공유하는 앱 세션 토큰이다. */
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
)
