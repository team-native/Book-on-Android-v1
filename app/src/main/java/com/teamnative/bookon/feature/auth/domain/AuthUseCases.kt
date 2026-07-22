package com.teamnative.bookon.feature.auth.domain

import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    /** 로그인 버튼 클릭 시 서버 세션을 발급한다. */
    suspend operator fun invoke(loginId: String, password: String) = repository.login(loginId, password)
}
class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    /** 사용자가 로그아웃할 때 refresh token으로 서버 로그인 세션 폐기를 요청한다. */
    suspend operator fun invoke(refreshToken: String) = repository.logout(refreshToken)
}
class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    /** 비밀번호 입력 완료 시 가입 인증 메일을 요청하고 인증 세션을 반환한다. */
    suspend operator fun invoke(draft: RegistrationDraft) = repository.register(draft)
}
class VerifyRegistrationUseCase @Inject constructor(private val repository: AuthRepository) {
    /** 인증 코드 입력 완료 시 가입 세션을 검증한다. */
    suspend operator fun invoke(sessionId: String, passcode: String) = repository.verifyRegistration(sessionId, passcode)
}
class SendPasswordResetEmailUseCase @Inject constructor(private val repository: AuthRepository) {
    /** 재설정 첫 단계에서 인증 메일을 발송한다. */
    suspend operator fun invoke(email: String) = repository.sendPasswordResetEmail(email)
}
class ResetPasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    /** 인증번호와 새 비밀번호로 비밀번호를 변경한다. */
    suspend operator fun invoke(email: String, code: String, password: String, confirm: String) = repository.resetPassword(email, code, password, confirm)
}
class LinkRead365UseCase @Inject constructor(private val repository: AuthRepository) {
    /** Read365 연동 완료 버튼에서 계정 세션을 연결한다. */
    suspend operator fun invoke(id: String, password: String) = repository.linkRead365(id, password)
}
