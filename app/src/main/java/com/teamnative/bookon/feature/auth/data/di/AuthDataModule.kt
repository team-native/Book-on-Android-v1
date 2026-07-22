package com.teamnative.bookon.feature.auth.data.di

import com.teamnative.bookon.feature.auth.data.AuthRemoteDataSource
import com.teamnative.bookon.feature.auth.data.AuthRemoteDataSourceImpl
import com.teamnative.bookon.feature.auth.data.AuthRepositoryImpl
import com.teamnative.bookon.feature.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module @InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
    @Binds abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
