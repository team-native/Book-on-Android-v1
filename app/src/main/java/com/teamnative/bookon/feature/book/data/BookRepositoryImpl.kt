package com.teamnative.bookon.feature.book.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.Book
import com.teamnative.bookon.feature.book.domain.BookCategory
import com.teamnative.bookon.feature.book.domain.BookDetail
import com.teamnative.bookon.feature.book.domain.BookPage
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.Loan
import com.teamnative.bookon.feature.book.domain.LoanExtension
import com.teamnative.bookon.feature.book.domain.PurchaseLink
import com.teamnative.bookon.feature.book.domain.TodayRecommendation
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val remote: BookRemoteDataSource) : BookRepository {
    override suspend fun books(page: Int, size: Int, sort: BookSort, category: String?) = remote.books(page, size, sort.name, category).map { it.toDomain() }
    override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int) = remote.search(keyword, libraryNumber, page, size).map { it.toDomain() }
    override suspend fun newBooks(page: Int, size: Int) = remote.newBooks(page, size).map { it.toDomain() }
    override suspend fun categories() = remote.categories().map { response ->
        response.items.map { category ->
            BookCategory(
                categoryId = category.categoryId,
                code = category.code,
                name = category.name,
                bookCount = category.bookCount,
            )
        }
    }
    override suspend fun todayRecommendations() = remote.todayRecommendations().map { response ->
        response.items.map { item ->
            TodayRecommendation(
                bookId = item.bookId,
                title = item.title,
                author = item.author,
                coverImageUrl = item.coverUrl ?: item.coverImageUrl,
                reason = item.reason,
            )
        }
    }
    override suspend fun purchaseLinks(bookId: Long) = remote.purchaseLinks(bookId).map { response ->
        response.items.map { PurchaseLink(it.provider, it.label, it.url) }
    }
    override suspend fun book(bookId: Long) = remote.book(bookId).map { dto -> BookDetail(dto.toBook(), dto.description, dto.favorite, dto.locationName, dto.returnPlanDate) }
    override suspend fun favorite(bookId: Long, favorite: Boolean) = remote.favorite(bookId, favorite).map { it.favorite }
    override suspend fun loan(bookId: Long) = remote.loan(bookId).map {
        Loan(it.loanId, it.bookId, it.dueDate, it.status, it.title, it.extensionAvailable)
    }
    override suspend fun extendLoan(loanId: Long) = remote.extendLoan(loanId).map {
        LoanExtension(it.loanId, it.previousDueDate, it.newDueDate, it.extensionCount, it.extensionAvailable)
    }
}
private fun BookPageDto.toDomain() = BookPage(
    items = items.map { it.toBook() },
    page = pagination.page,
    hasNext = pagination.hasNext,
    totalCount = pagination.totalCount,
)

private fun BookDto.toBook() = Book(
    id = bookId,
    title = title,
    author = author,
    publisher = publisher,
    category = category,
    libraryNumber = libraryNumber,
    coverImageUrl = coverUrl ?: coverImageUrl,
    loanAvailable = loanAvailable,
    status = status,
)

private fun BookDetailDto.toBook() = Book(
    id = bookId,
    title = title,
    author = author,
    publisher = publisher,
    category = category,
    libraryNumber = libraryNumber,
    coverImageUrl = coverUrl ?: coverImageUrl,
    loanAvailable = loanAvailable,
    status = status,
)

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
