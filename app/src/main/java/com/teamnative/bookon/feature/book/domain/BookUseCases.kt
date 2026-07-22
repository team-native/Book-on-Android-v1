package com.teamnative.bookon.feature.book.domain

import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke(
        page: Int,
        size: Int,
        sort: BookSort,
        category: String?,
    ) = repository.books(page, size, sort, category)
}

class GetBookCategoriesUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke() = repository.categories()
}

class GetNewBooksUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke(page: Int, size: Int) = repository.newBooks(page, size)
}

class SearchBooksUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke(
        keyword: String?,
        libraryNumber: String?,
        page: Int,
        size: Int,
    ) = repository.search(keyword, libraryNumber, page, size)
}

class GetBookDetailUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke(bookId: Long) = repository.book(bookId)
}

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke(bookId: Long, favorite: Boolean) = repository.favorite(bookId, favorite)
}

class RequestLoanUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend operator fun invoke(bookId: Long) = repository.loan(bookId)
}
