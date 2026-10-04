package com.teamnative.bookon.feature.notification.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.teamnative.bookon.feature.notification.domain.BookOnNotification

@Composable
fun BookOnNotificationsRoute(
    onBackClick: () -> Unit,
    onRelatedClick: (BookOnNotification) -> Unit,
    viewModel: BookOnNotificationsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LifecycleResumeEffect(viewModel) {
        viewModel.refresh()
        onPauseOrDispose { }
    }
    BookOnNotificationsScreen(uiState) { event ->
        when (event) {
            BookOnNotificationsScreenEvent.Back -> onBackClick()
            BookOnNotificationsScreenEvent.Retry -> viewModel.retry()
            BookOnNotificationsScreenEvent.LoadMore -> viewModel.loadNextPage()
            BookOnNotificationsScreenEvent.ReadAll -> viewModel.readAll()
            is BookOnNotificationsScreenEvent.RetryRead -> viewModel.retryRead(event.id)
            is BookOnNotificationsScreenEvent.Expand -> viewModel.expand(event.id)
            is BookOnNotificationsScreenEvent.Related -> onRelatedClick(event.notification)
        }
    }
}

sealed interface BookOnNotificationsScreenEvent {
    data object Back : BookOnNotificationsScreenEvent
    data object Retry : BookOnNotificationsScreenEvent
    data object LoadMore : BookOnNotificationsScreenEvent
    data object ReadAll : BookOnNotificationsScreenEvent
    data class RetryRead(val id: Long) : BookOnNotificationsScreenEvent
    data class Expand(val id: Long) : BookOnNotificationsScreenEvent
    data class Related(val notification: BookOnNotification) : BookOnNotificationsScreenEvent
}
