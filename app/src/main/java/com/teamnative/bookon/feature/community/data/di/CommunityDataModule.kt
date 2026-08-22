package com.teamnative.bookon.feature.community.data.di

import com.teamnative.bookon.feature.community.data.CommunityRemoteDataSource
import com.teamnative.bookon.feature.community.data.CommunityRemoteDataSourceImpl
import com.teamnative.bookon.feature.community.data.CommunityRepositoryImpl
import com.teamnative.bookon.feature.community.domain.CommunityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommunityDataModule {
    @Binds abstract fun bindCommunityRemoteDataSource(impl: CommunityRemoteDataSourceImpl): CommunityRemoteDataSource
    @Binds abstract fun bindCommunityRepository(impl: CommunityRepositoryImpl): CommunityRepository
}
