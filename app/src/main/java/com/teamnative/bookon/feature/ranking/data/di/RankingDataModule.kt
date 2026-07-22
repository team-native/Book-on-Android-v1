package com.teamnative.bookon.feature.ranking.data.di

import com.teamnative.bookon.feature.ranking.data.RankingRemoteDataSource
import com.teamnative.bookon.feature.ranking.data.RankingRemoteDataSourceImpl
import com.teamnative.bookon.feature.ranking.data.RankingRepositoryImpl
import com.teamnative.bookon.feature.ranking.domain.RankingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RankingDataModule {
    @Binds abstract fun bindRankingRemoteDataSource(implementation: RankingRemoteDataSourceImpl): RankingRemoteDataSource
    @Binds abstract fun bindRankingRepository(implementation: RankingRepositoryImpl): RankingRepository
}
