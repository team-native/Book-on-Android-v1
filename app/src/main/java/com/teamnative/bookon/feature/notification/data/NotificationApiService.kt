package com.teamnative.bookon.feature.notification.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.serialization.json.JsonObject
import retrofit2.http.PATCH
import retrofit2.http.Path

/** 명세에서 응답 필드가 확정된 알림 읽음 처리 HTTP 계약이다. */
interface NotificationApiService {
    @GET("me/notifications")
    suspend fun notifications(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiEnvelope<NotificationPageDto>>

    @GET("me/notifications/unread-count")
    suspend fun unreadCount(): Response<ApiEnvelope<NotificationUnreadCountDto>>

    @PATCH("me/notifications/{id}/read")
    suspend fun markRead(
        @Path("id") notificationId: Long,
    ): Response<ApiEnvelope<NotificationReadResponseDto>>

    @PATCH("me/notifications/read-all")
    suspend fun markAllRead(): Response<ApiEnvelope<NotificationReadAllResponseDto>>
}

@Serializable
data class NotificationReadResponseDto(
    @SerialName("id") val notificationId: Long,
    @SerialName("isRead") val read: Boolean,
)

@Serializable
data class NotificationReadAllResponseDto(
    @SerialName("updated") val updated: Boolean,
)

@Serializable
data class NotificationPageDto(
    @SerialName("notifications") val notifications: List<NotificationDto>,
    @SerialName("pagination") val pagination: com.teamnative.bookon.core.network.ApiPagination,
)

@Serializable
data class NotificationDto(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("isRead") val isRead: Boolean,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("deepLink") val deepLink: String? = null,
    @SerialName("payload") val payload: JsonObject? = null,
)

@Serializable
data class NotificationUnreadCountDto(
    @SerialName("count") val unreadCount: Int,
)
