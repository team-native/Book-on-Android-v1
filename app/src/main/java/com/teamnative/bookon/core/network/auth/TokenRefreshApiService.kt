package com.teamnative.bookon.core.network.auth

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** Authenticator 전용 token refresh HTTP 계약이다. */
interface TokenRefreshApiService {
    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshTokenRequestDto,
    ): Response<ApiEnvelope<RefreshTokenResponseDto>>
}

@Serializable
data class RefreshTokenRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class RefreshTokenResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("tokenType")
    val tokenType: String? = null,
    @SerialName("expiresIn")
    val expiresIn: Long? = null,
)
