package com.teamnative.bookon.feature.book.data.di

import com.teamnative.bookon.feature.book.data.BookRemoteDataSource
import com.teamnative.bookon.feature.book.data.BookRemoteDataSourceImpl
import com.teamnative.bookon.feature.book.data.BookRepositoryImpl
import com.teamnative.bookon.feature.book.domain.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module @InstallIn(SingletonComponent::class)
abstract class BookDataModule {
    @Binds abstract fun bindBookRemoteDataSource(implementation: BookRemoteDataSourceImpl): BookRemoteDataSource
    @Binds abstract fun bindBookRepository(implementation: BookRepositoryImpl): BookRepository
}
