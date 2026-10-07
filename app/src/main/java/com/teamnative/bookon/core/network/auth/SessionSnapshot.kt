package com.teamnative.bookon.core.network.auth

/** 요청이 시작된 로그인 세션을 식별하며 토큰 값을 문자열로 출력하지 않는다. */
class SessionSnapshot(
    val epoch: Long,
    val tokens: AuthTokens?,
)
