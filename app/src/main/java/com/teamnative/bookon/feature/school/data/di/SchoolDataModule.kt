package com.teamnative.bookon.feature.school.data.di

import com.teamnative.bookon.feature.school.data.SchoolRemoteDataSource
import com.teamnative.bookon.feature.school.data.SchoolRemoteDataSourceImpl
import com.teamnative.bookon.feature.school.data.SchoolRepositoryImpl
import com.teamnative.bookon.feature.school.domain.SchoolRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SchoolDataModule {
    @Binds abstract fun bindSchoolRemoteDataSource(impl: SchoolRemoteDataSourceImpl): SchoolRemoteDataSource
    @Binds abstract fun bindSchoolRepository(impl: SchoolRepositoryImpl): SchoolRepository
}
