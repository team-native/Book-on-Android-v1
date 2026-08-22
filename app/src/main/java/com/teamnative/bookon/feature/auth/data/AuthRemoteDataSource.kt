package com.teamnative.bookon.feature.auth.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface AuthRemoteDataSource {
    suspend fun register(request: RegisterRequestDto): NetworkResult<RegisterResponseDto>
    suspend fun verifyRegistration(sessionId: String, passcode: String): NetworkResult<RegistrationResponseDto>
    suspend fun login(id: String, password: String): NetworkResult<LoginResponseDto>
    suspend fun logout(refreshToken: String): NetworkResult<Unit>
    suspend fun sendReset(email: String): NetworkResult<PasswordResetEmailResponseDto>
    suspend fun reset(email: String, code: String, password: String, confirm: String): NetworkResult<PasswordResetResponseDto>
    suspend fun linkRead365(id: String, password: String): NetworkResult<Read365LoginResponseDto>
}
class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApiService,
    private val read365Api: Read365ApiService,
    private val executor: ApiExecutor,
) : AuthRemoteDataSource {
    override suspend fun register(request: RegisterRequestDto) = executor.execute { api.register(request) }
    override suspend fun verifyRegistration(sessionId: String, passcode: String) = executor.execute { api.verifyRegistration(VerifyRegistrationRequestDto(sessionId, passcode)) }
    override suspend fun login(id: String, password: String) = executor.execute { api.login(LoginRequestDto(id, password)) }
    override suspend fun logout(refreshToken: String) = executor.executeUnit { api.logout(LogoutRequestDto(refreshToken)) }
    override suspend fun sendReset(email: String) = executor.execute { api.sendReset(EmailRequestDto(email)) }
    override suspend fun reset(email: String, code: String, password: String, confirm: String) = executor.execute { api.reset(ResetPasswordRequestDto(email, code, password, confirm)) }
    override suspend fun linkRead365(id: String, password: String) = executor.execute { read365Api.linkRead365(Read365LoginRequestDto(id, password)) }
}
