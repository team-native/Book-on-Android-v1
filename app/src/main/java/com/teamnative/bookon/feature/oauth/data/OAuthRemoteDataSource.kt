package com.teamnative.bookon.feature.oauth.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface OAuthRemoteDataSource {
    suspend fun authorizationUrl(provider: String): NetworkResult<OAuthAuthorizationUrlDto>
    suspend fun callback(provider: String, code: String, state: String): NetworkResult<OAuthCallbackResponseDto>
}

class OAuthRemoteDataSourceImpl @Inject constructor(
    private val authenticatedApi: OAuthAuthenticatedApiService,
    private val publicApi: OAuthPublicApiService,
    private val executor: ApiExecutor,
) : OAuthRemoteDataSource {
    /** 로그인한 사용자의 외부 계정 연동 URL과 CSRF state를 발급받는다. */
    override suspend fun authorizationUrl(provider: String) = executor.execute {
        authenticatedApi.authorizationUrl(provider)
    }

    /** OAuth provider의 code/state를 서버에 전달해 로그인 또는 연동 결과를 받는다. */
    override suspend fun callback(provider: String, code: String, state: String) = executor.execute {
        publicApi.callback(provider, OAuthCallbackRequestDto(code, state))
    }
}
