package com.teamnative.bookon.feature.fcm.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

/** 실서버 명세(POST/DELETE /me/fcm-token)에 맞춘 FCM 등록 토큰 등록·해제 HTTP 계약이다. */
interface FcmApiService {
    @POST("me/fcm-token")
    suspend fun registerToken(
        @Body request: FcmTokenRegisterRequestDto,
    ): Response<ApiEnvelope<FcmTokenRegisterResponseDto>>

    // Retrofit의 @DELETE는 @Body를 지원하지 않으므로 hasBody=true인 @HTTP를 사용한다.
    @HTTP(method = "DELETE", path = "me/fcm-token", hasBody = true)
    suspend fun unregisterToken(
        @Body request: FcmTokenUnregisterRequestDto,
    ): Response<ApiEnvelope<FcmTokenUnregisterResponseDto>>
}

@Serializable
data class FcmTokenRegisterRequestDto(
    @SerialName("token")
    val token: String,
    @SerialName("dlsUserKey")
    val dlsUserKey: String? = null,
    @SerialName("platform")
    val platform: String? = null,
    @SerialName("deviceId")
    val deviceId: String? = null,
)

@Serializable
data class FcmTokenRegisterResponseDto(
    @SerialName("registered")
    val registered: Boolean,
)

@Serializable
data class FcmTokenUnregisterRequestDto(
    @SerialName("token")
    val token: String,
)

@Serializable
data class FcmTokenUnregisterResponseDto(
    @SerialName("unregistered")
    val unregistered: Boolean,
)
