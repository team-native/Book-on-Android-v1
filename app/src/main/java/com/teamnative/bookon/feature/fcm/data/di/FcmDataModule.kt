package com.teamnative.bookon.feature.fcm.data.di

import com.teamnative.bookon.feature.fcm.data.FcmRemoteDataSource
import com.teamnative.bookon.feature.fcm.data.FcmRemoteDataSourceImpl
import com.teamnative.bookon.feature.fcm.data.FcmRepositoryImpl
import com.teamnative.bookon.feature.fcm.domain.FcmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FcmDataModule {
    @Binds abstract fun bindFcmRemoteDataSource(impl: FcmRemoteDataSourceImpl): FcmRemoteDataSource
    @Binds abstract fun bindFcmRepository(impl: FcmRepositoryImpl): FcmRepository
}
