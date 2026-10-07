package com.teamnative.bookon.core.network.auth

import kotlinx.coroutines.CompletableDeferred
import com.teamnative.bookon.feature.auth.domain.SessionCleanupHandle

/** Data 계층의 정리 요청에만 이전 자격 증명을 제공한다. */
class SessionCleanupAuthorization internal constructor(
    tokens: AuthTokens?,
    internal val previousCleanup: CompletableDeferred<Unit>?,
    internal val completion: CompletableDeferred<Unit>,
) : SessionCleanupHandle {
    @Volatile
    private var credentials: AuthTokens? = tokens

    internal fun accessToken(): String? = credentials?.accessToken
    internal fun refreshToken(): String? = credentials?.refreshToken

    override fun dispose() {
        credentials = null
    }
}
