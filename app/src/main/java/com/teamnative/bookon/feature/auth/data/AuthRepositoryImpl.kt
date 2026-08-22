package com.teamnative.bookon.feature.auth.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.AuthRepository
import com.teamnative.bookon.feature.auth.domain.LoginSession
import com.teamnative.bookon.feature.auth.domain.RegistrationDraft
import com.teamnative.bookon.feature.auth.domain.RegistrationSession
import com.teamnative.bookon.feature.auth.domain.RegisteredUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remote: AuthRemoteDataSource) : AuthRepository {
    override suspend fun register(request: RegistrationDraft): NetworkResult<RegistrationSession> = when (
        val result = remote.register(request.toRegisterRequestDto())
    ) {
        is NetworkResult.Success -> NetworkResult.Success(RegistrationSession(result.data.sessionId, result.data.expiresAt, result.data.email))
        is NetworkResult.Failure -> result
    }
    override suspend fun verifyRegistration(sessionId: String, passcode: String): NetworkResult<RegisteredUser> = when (val result = remote.verifyRegistration(sessionId, passcode)) {
        is NetworkResult.Success -> NetworkResult.Success(RegisteredUser(result.data.userId, result.data.email, result.data.name))
        is NetworkResult.Failure -> result
    }
    override suspend fun login(loginId: String, password: String): NetworkResult<LoginSession> = when (val result = remote.login(loginId, password)) {
        is NetworkResult.Success -> NetworkResult.Success(
            LoginSession(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken,
                userId = result.data.userId,
                name = result.data.name,
                email = result.data.email,
                tokenType = result.data.tokenType,
                expiresIn = result.data.expiresIn,
            ),
        )
        is NetworkResult.Failure -> result
    }
    override suspend fun logout(refreshToken: String): NetworkResult<Unit> = remote.logout(refreshToken)
    override suspend fun sendPasswordResetEmail(email: String): NetworkResult<Unit> = remote.sendReset(email).toUnit()
    override suspend fun resetPassword(email: String, code: String, password: String, passwordConfirm: String): NetworkResult<Unit> = remote.reset(email, code, password, passwordConfirm).toUnit()
    override suspend fun linkRead365(id: String, password: String): NetworkResult<Unit> = remote.linkRead365(id, password).toUnit()
    private fun <T> NetworkResult<T>.toUnit(): NetworkResult<Unit> = when (this) {
        is NetworkResult.Success -> NetworkResult.Success(Unit)
        is NetworkResult.Failure -> this
    }

    /** 회원가입 화면의 학번 입력값을 서버가 요구하는 전체 학교 이메일 주소로 변환한다. */
    private fun RegistrationDraft.toRegisterRequestDto(): RegisterRequestDto = RegisterRequestDto(
        email = email.toGsmSchoolEmail(),
        name = name,
        department = department,
        gender = gender,
        password = password,
        passwordConfirm = passwordConfirm,
    )

    /** 사용자가 이미 전체 이메일을 입력한 경우에는 도메인을 중복해서 붙이지 않는다. */
    private fun String.toGsmSchoolEmail(): String {
        val normalizedEmail = trim()
        return if (normalizedEmail.endsWith(GsmSchoolEmailDomain, ignoreCase = true)) {
            normalizedEmail
        } else {
            normalizedEmail + GsmSchoolEmailDomain
        }
    }

    private companion object {
        const val GsmSchoolEmailDomain = "@gsm.hs.kr"
    }
}
