package com.teamnative.bookon.core.notification.di

import com.teamnative.bookon.core.notification.FcmTokenProvider
import com.teamnative.bookon.core.notification.FcmTokenProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationPlatformModule {
    @Binds abstract fun bindFcmTokenProvider(impl: FcmTokenProviderImpl): FcmTokenProvider
}
