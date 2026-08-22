package com.teamnative.bookon.feature.system.data.di

import com.teamnative.bookon.feature.system.data.HealthRemoteDataSource
import com.teamnative.bookon.feature.system.data.HealthRemoteDataSourceImpl
import com.teamnative.bookon.feature.system.data.HealthRepositoryImpl
import com.teamnative.bookon.feature.system.domain.HealthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SystemDataModule {
    @Binds abstract fun bindHealthRemoteDataSource(impl: HealthRemoteDataSourceImpl): HealthRemoteDataSource
    @Binds abstract fun bindHealthRepository(impl: HealthRepositoryImpl): HealthRepository
}
