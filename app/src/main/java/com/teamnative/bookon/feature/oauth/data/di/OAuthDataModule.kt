package com.teamnative.bookon.feature.oauth.data.di

import com.teamnative.bookon.feature.oauth.data.OAuthRemoteDataSource
import com.teamnative.bookon.feature.oauth.data.OAuthRemoteDataSourceImpl
import com.teamnative.bookon.feature.oauth.data.OAuthRepositoryImpl
import com.teamnative.bookon.feature.oauth.domain.OAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OAuthDataModule {
    @Binds abstract fun bindOAuthRemoteDataSource(impl: OAuthRemoteDataSourceImpl): OAuthRemoteDataSource
    @Binds abstract fun bindOAuthRepository(impl: OAuthRepositoryImpl): OAuthRepository
}
