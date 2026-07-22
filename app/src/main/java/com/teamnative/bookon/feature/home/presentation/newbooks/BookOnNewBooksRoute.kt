package com.teamnative.bookon.feature.home.presentation.newbooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen

/** 신간 목록 서버 상태와 뒤로가기 탐색을 연결한다. */
@Composable
fun BookOnNewBooksRoute(
    onBackClick: () -> Unit,
    viewModel: BookOnNewBooksViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnNewBooksScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onRetryClick = viewModel::retry,
            onLoadMoreClick = viewModel::loadNextPage,
        )
    }
}
