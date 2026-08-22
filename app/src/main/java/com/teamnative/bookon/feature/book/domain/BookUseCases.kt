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

class GetTodayRecommendationsUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    /** 도서 추천 화면에서 오늘의 추천 목록을 조회한다. */
    suspend operator fun invoke() = repository.todayRecommendations()
}

class GetPurchaseLinksUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    /** 도서 상세에서 외부 구매 링크 후보를 조회한다. */
    suspend operator fun invoke(bookId: Long) = repository.purchaseLinks(bookId)
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

class ExtendLoanUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    /** 현재 대출의 연장 가능 여부와 새 반납일을 서버에 확인한다. */
    suspend operator fun invoke(loanId: Long) = repository.extendLoan(loanId)
}
