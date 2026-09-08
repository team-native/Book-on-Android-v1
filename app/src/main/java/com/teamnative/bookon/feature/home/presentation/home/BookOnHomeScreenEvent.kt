package com.teamnative.bookon.feature.home.presentation.home

/** Home Screen에서 Route로 전달하는 사용자 의도이다. */
sealed interface BookOnHomeScreenEvent {
    data object SearchClicked : BookOnHomeScreenEvent
    data object NotificationClicked : BookOnHomeScreenEvent
    data object PopularBooksMoreClicked : BookOnHomeScreenEvent
    data object RetryClicked : BookOnHomeScreenEvent
    data class BookClicked(val bookId: Long) : BookOnHomeScreenEvent
}
