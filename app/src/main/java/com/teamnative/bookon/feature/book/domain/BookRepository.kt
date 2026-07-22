package com.teamnative.bookon.feature.book.domain

import com.teamnative.bookon.core.network.NetworkResult

interface BookRepository {
    suspend fun books(page: Int, size: Int, sort: BookSort, category: String?): NetworkResult<BookPage>
    suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int): NetworkResult<BookPage>
    suspend fun newBooks(page: Int, size: Int): NetworkResult<BookPage>
    suspend fun categories(): NetworkResult<List<BookCategory>>
    suspend fun book(bookId: Long): NetworkResult<BookDetail>
    suspend fun favorite(bookId: Long, favorite: Boolean): NetworkResult<Boolean>
    suspend fun loan(bookId: Long): NetworkResult<Loan>
}
enum class BookSort { POPULAR, NEW }
data class Book(val id: Long, val title: String, val author: String, val publisher: String, val category: String, val libraryNumber: String, val coverImageUrl: String?, val loanAvailable: Boolean, val status: String)
data class BookPage(val items: List<Book>, val page: Int, val hasNext: Boolean, val totalCount: Int)
data class BookCategory(val code: String, val name: String)
data class BookDetail(val book: Book, val description: String?, val favorite: Boolean, val locationName: String?, val returnPlanDate: String?)
data class Loan(val id: Long, val bookId: Long, val dueDate: String, val status: String, val title: String)
