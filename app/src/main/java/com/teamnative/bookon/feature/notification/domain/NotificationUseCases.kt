package com.teamnative.bookon.feature.notification.domain

import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {
    /** 사용자가 선택한 알림 하나를 읽음 처리한다. */
    suspend operator fun invoke(notificationId: Long) = repository.markRead(notificationId)
}

class MarkAllNotificationsReadUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {
    /** 사용자의 모든 알림을 읽음 처리하고 변경 건수를 반환한다. */
    suspend operator fun invoke() = repository.markAllRead()
}
