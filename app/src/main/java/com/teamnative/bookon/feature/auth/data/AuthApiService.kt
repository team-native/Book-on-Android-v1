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
    ): Response<ApiEnvelope<PasswordResetEmailResponseDto>>

    @PATCH("auth/password-reset")
    suspend fun reset(
        @Body body: ResetPasswordRequestDto,
    ): Response<ApiEnvelope<PasswordResetResponseDto>>
}

/** 로그인한 사용자에게만 허용되는 read365 연동 HTTP 계약이다. */
interface Read365ApiService {
    @POST("auth/read365/login")
    suspend fun linkRead365(
        @Body body: Read365LoginRequestDto,
    ): Response<ApiEnvelope<Read365LoginResponseDto>>

    @POST("auth/read365/session")
    suspend fun registerRead365Session(
        @Body body: Read365SessionRequestDto,
    ): Response<ApiEnvelope<Read365SessionResponseDto>>

    @POST("auth/read365/session/extend")
    suspend fun extendRead365Session(): Response<ApiEnvelope<Read365SessionResponseDto>>
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
    @SerialName("userId")
    val userId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("tokenType")
    val tokenType: String,
    @SerialName("expiresIn")
    val expiresIn: Long,
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
data class Read365SessionRequestDto(
    @SerialName("cookieHeader")
    val cookieHeader: String,
    @SerialName("read365Id")
    val read365Id: String? = null,
    @SerialName("sessionExpiresAt")
    val sessionExpiresAt: String? = null,
)

/** Read365 응답에서 명세에 필드가 정의된 세션 정보만 보존한다. profile은 별도 스키마가 없어 제외한다. */
@Serializable
data class Read365LoginResponseDto(
    @SerialName("read365Id")
    val read365Id: String,
    @SerialName("cookie")
    val cookie: String? = null,
    @SerialName("sessionExpiresAt")
    val sessionExpiresAt: String? = null,
    @SerialName("jsessionId")
    val jsessionId: String? = null,
)

@Serializable
data class Read365SessionResponseDto(
    @SerialName("read365Id")
    val read365Id: String,
    @SerialName("sessionExpiresAt")
    val sessionExpiresAt: String? = null,
)

@Serializable
data class PasswordResetEmailResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("expiresIn")
    val expiresIn: Long,
)

@Serializable
data class PasswordResetResponseDto(
    @SerialName("email")
    val email: String,
)

@Serializable
data class EmptyDto(
    @SerialName("ignored")
    val ignored: String? = null,
)
