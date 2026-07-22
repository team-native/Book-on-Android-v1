package com.teamnative.bookon.feature.auth.data

import com.teamnative.bookon.core.network.ApiEnvelope
import com.teamnative.bookon.core.network.EmptyResponseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequestDto,
    ): Response<ApiEnvelope<RegisterResponseDto>>

    @POST("auth/register/verify")
    suspend fun verifyRegistration(
        @Body body: VerifyRegistrationRequestDto,
    ): Response<ApiEnvelope<RegistrationResponseDto>>

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto,
    ): Response<ApiEnvelope<LoginResponseDto>>

    @POST("auth/logout")
    suspend fun logout(
        @Body body: LogoutRequestDto,
    ): Response<ApiEnvelope<EmptyResponseDto>>

    @POST("auth/password-reset/email")
    suspend fun sendReset(
        @Body body: EmailRequestDto,
    ): Response<ApiEnvelope<EmptyDto>>

    @PATCH("auth/password-reset")
    suspend fun reset(
        @Body body: ResetPasswordRequestDto,
    ): Response<ApiEnvelope<EmptyDto>>
}

/** 로그인한 사용자에게만 허용되는 read365 연동 HTTP 계약이다. */
interface Read365ApiService {
    @POST("auth/read365/login")
    suspend fun linkRead365(
        @Body body: Read365LoginRequestDto,
    ): Response<ApiEnvelope<EmptyDto>>
}

@Serializable
data class RegisterRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("department")
    val department: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("password")
    val password: String,
    @SerialName("passwordConfirm")
    val passwordConfirm: String,
)

@Serializable
data class RegisterResponseDto(
    @SerialName("sessionId")
    val sessionId: String,
    @SerialName("expiresAt")
    val expiresAt: String,
    @SerialName("email")
    val email: String,
)

@Serializable
data class VerifyRegistrationRequestDto(
    @SerialName("sessionId")
    val sessionId: String,
    @SerialName("passcode")
    val passcode: String,
)

@Serializable
data class RegistrationResponseDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
)

@Serializable
data class LoginRequestDto(
    @SerialName("loginId")
    val loginId: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class LoginResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class LogoutRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class EmailRequestDto(
    @SerialName("email")
    val email: String,
)

@Serializable
data class ResetPasswordRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("verificationCode")
    val verificationCode: String,
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("newPasswordConfirm")
    val newPasswordConfirm: String,
)

@Serializable
data class Read365LoginRequestDto(
    @SerialName("id")
    val id: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class EmptyDto(
    @SerialName("ignored")
    val ignored: String? = null,
)
