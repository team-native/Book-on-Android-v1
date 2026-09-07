package com.teamnative.bookon.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.teamnative.bookon.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookOnNotificationChannelInstaller @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    /** 앱 시작 시 일반 알림 채널을 보장한다. 이미 존재하면 재호출은 no-op이다. */
    fun ensureChannelsCreated() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            GeneralChannelId,
            context.getString(R.string.notification_channel_general_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = context.getString(R.string.notification_channel_general_description)
        }
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val GeneralChannelId = "bookon_general_notifications"
    }
}
