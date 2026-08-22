package com.teamnative.bookon.feature.notification.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.notification.domain.NotificationRead
import com.teamnative.bookon.feature.notification.domain.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val remote: NotificationRemoteDataSource,
) : NotificationRepository {
    override suspend fun markRead(notificationId: Long): NetworkResult<NotificationRead> =
        remote.markRead(notificationId).map { NotificationRead(it.notificationId, it.read) }

    override suspend fun markAllRead(): NetworkResult<Int> =
        remote.markAllRead().map { it.updatedCount }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
