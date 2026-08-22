package com.teamnative.bookon.feature.auth.domain

import javax.inject.Inject

class LoginRead365UseCase @Inject constructor(
    private val repository: Read365Repository,
) {
    /** Read365 계정으로 외부 서비스 세션을 발급받는다. */
    suspend operator fun invoke(id: String, password: String) = repository.login(id, password)
}

class RegisterRead365SessionUseCase @Inject constructor(
    private val repository: Read365Repository,
) {
    /** 외부 WebView에서 얻은 Read365 Cookie 세션을 서버에 등록한다. */
    suspend operator fun invoke(
        cookieHeader: String,
        read365Id: String? = null,
        sessionExpiresAt: String? = null,
    ) = repository.registerSession(cookieHeader, read365Id, sessionExpiresAt)
}

class ExtendRead365SessionUseCase @Inject constructor(
    private val repository: Read365Repository,
) {
    /** 서버에 저장된 Read365 세션의 만료 연장을 요청한다. */
    suspend operator fun invoke() = repository.extendSession()
}
