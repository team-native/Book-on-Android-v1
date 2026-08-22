package com.teamnative.bookon.feature.notification.domain

import com.teamnative.bookon.core.network.NetworkResult

interface NotificationRepository {
    suspend fun markRead(notificationId: Long): NetworkResult<NotificationRead>
    suspend fun markAllRead(): NetworkResult<Int>
}

data class NotificationRead(val notificationId: Long, val read: Boolean)
