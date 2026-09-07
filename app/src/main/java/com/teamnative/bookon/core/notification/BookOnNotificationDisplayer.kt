package com.teamnative.bookon.core.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.teamnative.bookon.MainActivity
import com.teamnative.bookon.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookOnNotificationDisplayer @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    /** 알림 권한이 허용된 경우에만, 명시적 Intent와 최소 플래그로 로컬 알림을 표시한다. */
    fun show(
        title: String,
        body: String,
        notificationType: String?,
        notificationId: Int,
    ) {
        val hasPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            // SINGLE_TOP + CLEAR_TOP이어야 MainActivity가 이미 떠 있을 때 재생성 없이 onNewIntent로 전달된다.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if (notificationType != null) {
                // 백그라운드/종료 상태에서 시스템이 알림을 직접 표시할 때도 FCM data의 "type" 키가
                // 그대로 인텐트 extra에 실리므로, 여기서도 동일한 키 이름을 사용해 두 경로를 통일한다.
                putExtra(NotificationTypeExtraKey, notificationType)
            }
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val notification = NotificationCompat.Builder(context, BookOnNotificationChannelInstaller.GeneralChannelId)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    companion object {
        const val NotificationTypeExtraKey = "type"
    }
}
