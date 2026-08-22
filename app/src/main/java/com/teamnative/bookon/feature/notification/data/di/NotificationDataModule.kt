package com.teamnative.bookon.feature.notification.data.di

import com.teamnative.bookon.feature.notification.data.NotificationRemoteDataSource
import com.teamnative.bookon.feature.notification.data.NotificationRemoteDataSourceImpl
import com.teamnative.bookon.feature.notification.data.NotificationRepositoryImpl
import com.teamnative.bookon.feature.notification.domain.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationDataModule {
    @Binds abstract fun bindNotificationRemoteDataSource(impl: NotificationRemoteDataSourceImpl): NotificationRemoteDataSource
    @Binds abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
