package com.teamnative.bookon.feature.notification.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.notification.domain.NotificationRead
import com.teamnative.bookon.feature.notification.domain.NotificationRepository
import com.teamnative.bookon.feature.notification.domain.*
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val remote: NotificationRemoteDataSource,
) : NotificationRepository {
    override suspend fun notifications(page: Int, size: Int): NetworkResult<NotificationPage> =
        remote.notifications(page, size).map { response ->
            NotificationPage(
                items = response.notifications.map { notification ->
                    BookOnNotification(
                        id = notification.id,
                        kind = when (notification.type) {
                            "loan_due" -> NotificationKind.LoanDue
                            "notice" -> NotificationKind.Notice
                            "new_book" -> NotificationKind.NewBook
                            else -> NotificationKind.Unknown
                        },
                        title = notification.title,
                        body = notification.body,
                        isRead = notification.isRead,
                        createdAt = notification.createdAt,
                        bookId = (notification.payload?.get("bookId") as? kotlinx.serialization.json.JsonPrimitive)
                            ?.longOrNull?.takeIf { it > 0 },
                    )
                },
                page = response.pagination.page,
                hasNext = response.pagination.hasNext,
                totalCount = response.pagination.totalCount,
            )
        }

    override suspend fun unreadCount(): NetworkResult<Int> = remote.unreadCount().map {
        it.unreadCount
    }

    override suspend fun markRead(notificationId: Long): NetworkResult<NotificationRead> =
        remote.markRead(notificationId).map { NotificationRead(it.notificationId, it.read) }

    override suspend fun markAllRead(): NetworkResult<Boolean> =
        remote.markAllRead().map { it.updated }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
