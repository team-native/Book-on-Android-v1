package com.teamnative.bookon.feature.media.data.di

import com.teamnative.bookon.feature.media.data.ImageRemoteDataSource
import com.teamnative.bookon.feature.media.data.ImageRemoteDataSourceImpl
import com.teamnative.bookon.feature.media.data.ImageRepositoryImpl
import com.teamnative.bookon.feature.media.domain.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaDataModule {
    @Binds abstract fun bindImageRemoteDataSource(impl: ImageRemoteDataSourceImpl): ImageRemoteDataSource
    @Binds abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository
}
