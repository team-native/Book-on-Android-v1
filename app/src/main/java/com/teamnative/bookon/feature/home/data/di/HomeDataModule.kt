package com.teamnative.bookon.feature.home.data.di

import com.teamnative.bookon.feature.home.data.HomeRemoteDataSource
import com.teamnative.bookon.feature.home.data.HomeRemoteDataSourceImpl
import com.teamnative.bookon.feature.home.data.HomeRepositoryImpl
import com.teamnative.bookon.feature.home.domain.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataModule {
    @Binds abstract fun bindHomeRemoteDataSource(implementation: HomeRemoteDataSourceImpl): HomeRemoteDataSource
    @Binds abstract fun bindHomeRepository(implementation: HomeRepositoryImpl): HomeRepository
}
