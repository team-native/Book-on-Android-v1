package com.teamnative.bookon.feature.auth.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.RegistrationDraft
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthRepositoryImplTest {
    @Test
    fun `회원가입 요청은 학번 입력값에 학교 이메일 도메인을 붙여 전송한다`() = kotlinx.coroutines.test.runTest {
        val remoteDataSource = RecordingAuthRemoteDataSource()
        val repository = AuthRepositoryImpl(remoteDataSource)

        repository.register(registrationDraft(email = "s26031"))

        assertEquals("s26031@gsm.hs.kr", remoteDataSource.registerRequest?.email)
    }

    @Test
    fun `회원가입 요청은 이미 완성된 학교 이메일의 도메인을 중복하지 않는다`() = kotlinx.coroutines.test.runTest {
        val remoteDataSource = RecordingAuthRemoteDataSource()
        val repository = AuthRepositoryImpl(remoteDataSource)

        repository.register(registrationDraft(email = "s26031@gsm.hs.kr"))

        assertEquals("s26031@gsm.hs.kr", remoteDataSource.registerRequest?.email)
    }

    private fun registrationDraft(email: String) = RegistrationDraft(
        email = email,
        name = "홍길동",
        department = "AI",
        gender = "MALE",
        password = "Password1!",
        passwordConfirm = "Password1!",
    )
}

private class RecordingAuthRemoteDataSource : AuthRemoteDataSource {
    var registerRequest: RegisterRequestDto? = null

    override suspend fun register(request: RegisterRequestDto): NetworkResult<RegisterResponseDto> {
        registerRequest = request
        return NetworkResult.Success(
            RegisterResponseDto(
                sessionId = "registration-session",
                expiresAt = "2026-07-22T00:00:00Z",
                email = request.email,
            ),
        )
    }

    override suspend fun verifyRegistration(sessionId: String, passcode: String): NetworkResult<RegistrationResponseDto> =
        error("not used")

    override suspend fun login(id: String, password: String): NetworkResult<LoginResponseDto> = error("not used")

    override suspend fun logout(refreshToken: String): NetworkResult<Unit> = error("not used")

    override suspend fun sendReset(email: String): NetworkResult<PasswordResetEmailResponseDto> = error("not used")

    override suspend fun reset(
        email: String,
        code: String,
        password: String,
        confirm: String,
    ): NetworkResult<PasswordResetResponseDto> = error("not used")

    override suspend fun linkRead365(id: String, password: String): NetworkResult<Read365LoginResponseDto> = error("not used")
}
