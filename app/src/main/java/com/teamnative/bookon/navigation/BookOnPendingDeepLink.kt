package com.teamnative.bookon.navigation

internal data class BookOnPendingDeepLink(
    val destination: BookOnDestination,
    val token: Long,
)

/** 외부 payload는 알려진 목적지와 양의 도서 ID만 허용한다. */
internal fun String?.toPendingDeepLinkDestination(bookId: Long? = null): BookOnDestination? = when (this) {
    "loan_due" -> BookOnDestination.LoanHistory
    "new_book" -> bookId?.takeIf { it > 0 }?.let { BookOnDestination.BookDetail(it) }
    else -> null
}
