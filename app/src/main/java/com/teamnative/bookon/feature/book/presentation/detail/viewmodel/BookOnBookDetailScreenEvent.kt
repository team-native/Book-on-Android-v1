package com.teamnative.bookon.feature.book.presentation.detail.viewmodel

sealed interface BookOnBookDetailScreenEvent {
    data object BackClicked : BookOnBookDetailScreenEvent
    data object RetryClicked : BookOnBookDetailScreenEvent
    data object FavoriteClicked : BookOnBookDetailScreenEvent
}
