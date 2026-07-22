package com.teamnative.bookon.feature.marathon.data.di

import com.teamnative.bookon.feature.marathon.data.MarathonRemoteDataSource
import com.teamnative.bookon.feature.marathon.data.MarathonRemoteDataSourceImpl
import com.teamnative.bookon.feature.marathon.data.MarathonRepositoryImpl
import com.teamnative.bookon.feature.marathon.domain.MarathonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MarathonDataModule {
    @Binds abstract fun bindMarathonRemoteDataSource(implementation: MarathonRemoteDataSourceImpl): MarathonRemoteDataSource
    @Binds abstract fun bindMarathonRepository(implementation: MarathonRepositoryImpl): MarathonRepository
}
