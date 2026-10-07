package com.teamnative.bookon.feature.notification.domain

import com.teamnative.bookon.core.network.NetworkResult

interface NotificationRepository {
    suspend fun notifications(page: Int, size: Int): NetworkResult<NotificationPage>
    suspend fun unreadCount(): NetworkResult<Int>

    suspend fun markRead(notificationId: Long): NetworkResult<NotificationRead>
    suspend fun markAllRead(): NetworkResult<Boolean>
}

data class NotificationRead(val notificationId: Long, val read: Boolean)

enum class NotificationKind { LoanDue, Notice, NewBook, Unknown }

data class BookOnNotification(
    val id: Long,
    val kind: NotificationKind,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: String,
    val bookId: Long?,
)

data class NotificationPage(
    val items: List<BookOnNotification>,
    val page: Int,
    val hasNext: Boolean,
    val totalCount: Int,
)
