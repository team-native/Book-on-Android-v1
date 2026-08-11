package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** 홈 API 상태와 상단 탐색 이벤트를 조립한다. */
@Composable
fun BookOnHomeRoute(
    bottomBar: @Composable () -> Unit,
    onSearchClick: () -> Unit,
    onNewBooksClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: BookOnHomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BookOnHomeScreen(
        uiState = uiState,
        bottomBar = bottomBar,
        onEvent = { event ->
            when (event) {
                BookOnHomeScreenEvent.SearchClicked -> onSearchClick()
                BookOnHomeScreenEvent.ShowMoreClicked -> onNewBooksClick()
                BookOnHomeScreenEvent.NotificationClicked -> onNotificationClick()
                BookOnHomeScreenEvent.RetryNoticeClicked -> viewModel.retryNotice()
                BookOnHomeScreenEvent.RetryRecommendationClicked -> viewModel.retryRecommendation()
                BookOnHomeScreenEvent.RetryPopularBooksClicked -> viewModel.retryPopularBooks()
            }
        },
    )
}
