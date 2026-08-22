package com.teamnative.bookon.feature.notification.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface NotificationRemoteDataSource {
    suspend fun markRead(notificationId: Long): NetworkResult<NotificationReadResponseDto>
    suspend fun markAllRead(): NetworkResult<NotificationReadAllResponseDto>
}

class NotificationRemoteDataSourceImpl @Inject constructor(
    private val api: NotificationApiService,
    private val executor: ApiExecutor,
) : NotificationRemoteDataSource {
    /** 단일 알림의 읽음 상태를 서버에 반영한다. */
    override suspend fun markRead(notificationId: Long) = executor.execute { api.markRead(notificationId) }

    /** 로그인 사용자의 미확인 알림을 한 번에 읽음 처리한다. */
    override suspend fun markAllRead() = executor.execute { api.markAllRead() }
}
