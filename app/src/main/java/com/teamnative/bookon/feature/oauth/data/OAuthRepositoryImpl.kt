package com.teamnative.bookon.feature.oauth.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.oauth.domain.OAuthAuthorizationUrl
import com.teamnative.bookon.feature.oauth.domain.OAuthCallback
import com.teamnative.bookon.feature.oauth.domain.OAuthRepository
import javax.inject.Inject

class OAuthRepositoryImpl @Inject constructor(
    private val remote: OAuthRemoteDataSource,
) : OAuthRepository {
    override suspend fun authorizationUrl(provider: String): NetworkResult<OAuthAuthorizationUrl> =
        remote.authorizationUrl(provider).map { OAuthAuthorizationUrl(it.provider, it.redirectUrl, it.state) }

    override suspend fun callback(provider: String, code: String, state: String): NetworkResult<OAuthCallback> =
        remote.callback(provider, code, state).map {
            OAuthCallback(it.userId, it.linked, it.accessToken, it.refreshToken)
        }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
