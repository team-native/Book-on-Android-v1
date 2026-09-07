package com.teamnative.bookon

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.teamnative.bookon.core.notification.BookOnNotificationChannelInstaller
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/** 앱 전체 의존성 그래프를 초기화하고, 커스텀 WorkManager 설정과 알림 채널을 준비한다. */
@HiltAndroidApp
class BookOnApplication : Application(), Configuration.Provider {

    @Inject lateinit var hiltWorkerFactory: HiltWorkerFactory
    @Inject lateinit var notificationChannelInstaller: BookOnNotificationChannelInstaller

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        notificationChannelInstaller.ensureChannelsCreated()
    }
}
