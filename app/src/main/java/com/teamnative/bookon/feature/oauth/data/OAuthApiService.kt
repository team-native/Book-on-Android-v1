package com.teamnative.bookon.feature.oauth.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** OAuth 연동 시작과 공개 콜백 처리 HTTP 계약이다. */
interface OAuthAuthenticatedApiService {
    @GET("auth/oauth/{provider}/url")
    suspend fun authorizationUrl(
        @Path("provider") provider: String,
    ): Response<ApiEnvelope<OAuthAuthorizationUrlDto>>
}

interface OAuthPublicApiService {
    @POST("auth/oauth/{provider}/callback")
    suspend fun callback(
        @Path("provider") provider: String,
        @Body body: OAuthCallbackRequestDto,
    ): Response<ApiEnvelope<OAuthCallbackResponseDto>>
}

@Serializable
data class OAuthAuthorizationUrlDto(
    @SerialName("provider") val provider: String,
    @SerialName("redirectUrl") val redirectUrl: String,
    @SerialName("state") val state: String,
)

@Serializable
data class OAuthCallbackRequestDto(
    @SerialName("code") val code: String,
    @SerialName("state") val state: String,
)

@Serializable
data class OAuthCallbackResponseDto(
    @SerialName("userId") val userId: Long,
    @SerialName("linked") val linked: Boolean,
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
)
