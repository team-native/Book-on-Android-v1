package com.teamnative.bookon.feature.notification.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.http.PATCH

class NotificationApiContractTest {
    @Test
    fun `read uses authenticated me route`() {
        val method = NotificationApiService::class.java.methods.first { it.name == "markRead" }
        assertEquals("me/notifications/{id}/read", method.getAnnotation(PATCH::class.java).value)
    }

    @Test
    fun `server read response decodes`() {
        val response = Json.decodeFromString<NotificationReadResponseDto>("""{"id":7,"isRead":true}""")
        assertEquals(7L, response.notificationId)
        assertEquals(true, response.read)
    }
    @Test
    fun `list and count preserve exact server keys`() {
        val page = Json.decodeFromString<NotificationPageDto>(
            """{"notifications":[{"id":7,"type":"new_book","title":"title","body":"body","isRead":false,"createdAt":"date","payload":{"bookId":"8"}}],"pagination":{"page":1,"size":20,"totalCount":21,"totalPages":2,"hasNext":true}}""",
        )
        assertEquals(7L, page.notifications.single().id)
        assertEquals(true, page.pagination.hasNext)
        assertEquals(21, page.pagination.totalCount)
        assertEquals(3, Json.decodeFromString<NotificationUnreadCountDto>("""{"count":3}""").unreadCount)
        assertEquals(false, Json.decodeFromString<NotificationReadAllResponseDto>("""{"updated":false}""").updated)
    }
}
