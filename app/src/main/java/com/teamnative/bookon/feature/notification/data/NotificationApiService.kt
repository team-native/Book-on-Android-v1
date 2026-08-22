package com.teamnative.bookon.feature.notification.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

/** 명세에서 응답 필드가 확정된 알림 읽음 처리 HTTP 계약이다. */
interface NotificationApiService {
    @PATCH("notifications/{notificationId}/read")
    suspend fun markRead(
        @Path("notificationId") notificationId: Long,
    ): Response<ApiEnvelope<NotificationReadResponseDto>>

    @PATCH("notifications/read-all")
    suspend fun markAllRead(): Response<ApiEnvelope<NotificationReadAllResponseDto>>
}

@Serializable
data class NotificationReadResponseDto(
    @SerialName("notificationId") val notificationId: Long,
    @SerialName("read") val read: Boolean,
)

@Serializable
data class NotificationReadAllResponseDto(
    @SerialName("updatedCount") val updatedCount: Int,
)
