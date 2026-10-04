package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.*

internal class RecoveryBookRepository : BookRepository {
    var onSearch: suspend (
        String?,
        Int
    ) -> NetworkResult<BookPage> = { _, _ ->

        page(1L)
    }
    var onNewBooks: suspend (Int) -> NetworkResult<BookPage> = {
        page(1L)
    }
    var onBooks: suspend (
        Int,
        String?
    ) -> NetworkResult<BookPage> = { _, _ ->

        page(1L)
    }
    var onCategories: suspend () -> NetworkResult<List<BookCategory>> = {
        NetworkResult.Success(emptyList())
    }
    var onDetail: suspend (Long) -> NetworkResult<BookDetail> = {
        detailResponse(it)
    }
    var onFavorite: suspend (
        Long,
        Boolean
    ) -> NetworkResult<Boolean> = { _, favorite ->

        NetworkResult.Success(favorite)
    }
    var onLoan: suspend (Long) -> NetworkResult<Loan> = {
        error("not used")
    }
    var onExtend: suspend (Long) -> NetworkResult<LoanExtension> = {
        error("not used")
    }
    override suspend fun books(
        page: Int,
        size: Int,
        sort: BookSort,
        category: String?
    ) = onBooks(
        page,
        category
    )
    override suspend fun search(
        keyword: String?,
        libraryNumber: String?,
        page: Int,
        size: Int
    ) = onSearch(
        keyword,
        page
    )
    override suspend fun newBooks(
        page: Int,
        size: Int
    ) = onNewBooks(page)
    override suspend fun categories() = onCategories()
    override suspend fun todayRecommendations(): NetworkResult<List<TodayRecommendation>> = error("not used")
    override suspend fun purchaseLinks(bookId: Long): NetworkResult<List<PurchaseLink>> = error("not used")
    override suspend fun book(bookId: Long) = onDetail(bookId)
    override suspend fun favorite(
        bookId: Long,
        favorite: Boolean
    ) = onFavorite(
        bookId,
        favorite
    )
    override suspend fun loan(bookId: Long) = onLoan(bookId)
    override suspend fun extendLoan(loanId: Long) = onExtend(loanId)
}

internal fun book(id: Long) = Book(
    id,
    "book $id",
    "author",
    "publisher",
    "category",
    "number",
    null,
    true,
    "AVAILABLE"
)
internal fun page(
    id: Long,
    hasNext: Boolean = false
) = NetworkResult.Success(BookPage(
        listOf(book(id)),
        if (id == 2L) 2 else 1,
        hasNext,
        1
))
internal fun detailResponse(id: Long) = NetworkResult.Success(BookDetail(
        book(id),
        "intro",
        false,
        "library",
        null
))
internal fun failure() = NetworkResult.Failure(NetworkError.Network(java.io.IOException("offline")))
