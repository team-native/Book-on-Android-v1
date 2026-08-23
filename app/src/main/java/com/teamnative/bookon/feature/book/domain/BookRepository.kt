package com.teamnative.bookon.feature.book.domain

import com.teamnative.bookon.core.network.NetworkResult

interface BookRepository {
    suspend fun books(page: Int, size: Int, sort: BookSort, category: String?): NetworkResult<BookPage>
    suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int): NetworkResult<BookPage>
    suspend fun newBooks(page: Int, size: Int): NetworkResult<BookPage>
    suspend fun categories(): NetworkResult<List<BookCategory>>
    suspend fun todayRecommendations(): NetworkResult<List<TodayRecommendation>>
    suspend fun purchaseLinks(bookId: Long): NetworkResult<List<PurchaseLink>>
    suspend fun book(bookId: Long): NetworkResult<BookDetail>
    suspend fun favorite(bookId: Long, favorite: Boolean): NetworkResult<Boolean>
    suspend fun loan(bookId: Long): NetworkResult<Loan>
    suspend fun extendLoan(loanId: Long): NetworkResult<LoanExtension>
}
enum class BookSort { POPULAR, NEW }
data class Book(val id: Long, val title: String, val author: String, val publisher: String, val category: String, val libraryNumber: String, val coverImageUrl: String?, val loanAvailable: Boolean, val status: String)
data class BookPage(val items: List<Book>, val page: Int, val hasNext: Boolean, val totalCount: Int)
data class BookCategory(
    val categoryId: Long,
    val code: String,
    val name: String,
    val bookCount: Int,
)
data class BookDetail(val book: Book, val description: String?, val favorite: Boolean, val locationName: String?, val returnPlanDate: String?)
data class Loan(
    val id: Long,
    val bookId: Long,
    val dueDate: String,
    val status: String,
    val title: String,
    val extensionAvailable: Boolean = false,
)
data class LoanExtension(
    val loanId: Long,
    val previousDueDate: String,
    val newDueDate: String,
    val extensionCount: Int,
    val extensionAvailable: Boolean,
)
data class TodayRecommendation(
    val bookId: Long,
    val title: String,
    val author: String,
    val coverImageUrl: String?,
    val reason: String?,
)
data class PurchaseLink(val provider: String, val label: String, val url: String)
