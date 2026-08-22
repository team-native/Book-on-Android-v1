package com.teamnative.bookon.feature.oauth.domain

import javax.inject.Inject

class GetOAuthAuthorizationUrlUseCase @Inject constructor(
    private val repository: OAuthRepository,
) {
    /** 외부 계정 연동을 시작할 provider URL을 요청한다. */
    suspend operator fun invoke(provider: String) = repository.authorizationUrl(provider)
}

class HandleOAuthCallbackUseCase @Inject constructor(
    private val repository: OAuthRepository,
) {
    /** OAuth callback의 일회성 code와 state를 서버에 전달한다. */
    suspend operator fun invoke(provider: String, code: String, state: String) =
        repository.callback(provider, code, state)
}
