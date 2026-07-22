package com.teamnative.bookon.feature.my.data.di

import com.teamnative.bookon.feature.my.data.MyRemoteDataSource
import com.teamnative.bookon.feature.my.data.MyRemoteDataSourceImpl
import com.teamnative.bookon.feature.my.data.MyRepositoryImpl
import com.teamnative.bookon.feature.my.domain.MyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module @InstallIn(SingletonComponent::class)
abstract class MyDataModule {
    @Binds abstract fun bindMyRemoteDataSource(implementation: MyRemoteDataSourceImpl): MyRemoteDataSource
    @Binds abstract fun bindMyRepository(implementation: MyRepositoryImpl): MyRepository
}
