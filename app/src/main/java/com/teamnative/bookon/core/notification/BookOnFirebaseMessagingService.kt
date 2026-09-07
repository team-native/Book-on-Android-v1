package com.teamnative.bookon.core.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookOnFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var notificationChannelInstaller: BookOnNotificationChannelInstaller
    @Inject lateinit var notificationDisplayer: BookOnNotificationDisplayer

    /** Firebase가 새 등록 토큰을 발급·갱신할 때 호출되며, 서버 동기화를 WorkManager에 위임한다(네트워크 직접 호출 금지). */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FcmTokenRegistrationWorker.enqueue(applicationContext, token)
    }

    /** 포그라운드 상태에서 도착한 메시지를 검증해 채널을 보장하고 로컬 알림으로 표시한다. */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notificationChannelInstaller.ensureChannelsCreated()

        val title = remoteMessage.notification?.title ?: remoteMessage.data[TitleKey]
        val body = remoteMessage.notification?.body ?: remoteMessage.data[BodyKey]
        if (title.isNullOrBlank() || body == null) {
            return
        }

        notificationDisplayer.show(
            title = title,
            body = body,
            deepLink = remoteMessage.data[DeepLinkKey],
            notificationId = (remoteMessage.messageId ?: title).hashCode(),
        )
    }

    private companion object {
        const val TitleKey = "title"
        const val BodyKey = "body"
        const val DeepLinkKey = "deepLink"
    }
}
