package com.teamnative.bookon.feature.book.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface BookRemoteDataSource {
    suspend fun books(page: Int, size: Int, sort: String, category: String?): NetworkResult<BookPageDto>
    suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int): NetworkResult<BookPageDto>
    suspend fun newBooks(page: Int, size: Int): NetworkResult<BookPageDto>
    suspend fun categories(): NetworkResult<CategoryListDto>
    suspend fun book(bookId: Long): NetworkResult<BookDetailDto>
    suspend fun favorite(bookId: Long, favorite: Boolean): NetworkResult<FavoriteDto>
    suspend fun loan(bookId: Long): NetworkResult<LoanDto>
}
class BookRemoteDataSourceImpl @Inject constructor(
    private val publicBookApiService: BookPublicApiService,
    private val authenticatedBookApiService: BookAuthenticatedApiService,
    private val executor: ApiExecutor,
) : BookRemoteDataSource {
    override suspend fun books(page: Int, size: Int, sort: String, category: String?) = executor.execute { publicBookApiService.books(page, size, sort, category) }
    override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int) = executor.execute { publicBookApiService.search(keyword, libraryNumber, page, size) }
    override suspend fun newBooks(page: Int, size: Int) = executor.execute { publicBookApiService.newBooks(page, size) }
    override suspend fun categories() = executor.execute { publicBookApiService.categories() }
    override suspend fun book(bookId: Long) = executor.execute { authenticatedBookApiService.book(bookId) }
    override suspend fun favorite(bookId: Long, favorite: Boolean) = executor.execute { if (favorite) authenticatedBookApiService.addFavorite(bookId) else authenticatedBookApiService.removeFavorite(bookId) }
    override suspend fun loan(bookId: Long) = executor.execute { authenticatedBookApiService.loan(LoanRequestDto(bookId)) }
}
