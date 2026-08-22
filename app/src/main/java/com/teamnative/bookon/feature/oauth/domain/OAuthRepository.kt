package com.teamnative.bookon.feature.oauth.domain

import com.teamnative.bookon.core.network.NetworkResult

interface OAuthRepository {
    suspend fun authorizationUrl(provider: String): NetworkResult<OAuthAuthorizationUrl>
    suspend fun callback(provider: String, code: String, state: String): NetworkResult<OAuthCallback>
}

data class OAuthAuthorizationUrl(val provider: String, val redirectUrl: String, val state: String)
data class OAuthCallback(
    val userId: Long,
    val linked: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
)
