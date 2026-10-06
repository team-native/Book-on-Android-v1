package com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel

sealed interface BookOnNewBooksScreenEvent {
    data object BackClicked : BookOnNewBooksScreenEvent
    data object RetryClicked : BookOnNewBooksScreenEvent
    data object LoadMoreClicked : BookOnNewBooksScreenEvent
    data class BookClicked(val bookId: Long) : BookOnNewBooksScreenEvent
}
