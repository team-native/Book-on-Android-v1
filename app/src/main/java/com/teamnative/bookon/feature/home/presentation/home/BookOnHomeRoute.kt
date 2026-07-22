package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen

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
    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnHomeScreen(
            uiState = uiState,
            bottomBar = bottomBar,
            onSearchClick = onSearchClick,
            onShowMoreClick = onNewBooksClick,
            onNotificationClick = onNotificationClick,
            onRetryClick = viewModel::load,
        )
    }
}
