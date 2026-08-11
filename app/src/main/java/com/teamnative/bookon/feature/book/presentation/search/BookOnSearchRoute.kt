package com.teamnative.bookon.feature.book.presentation.search

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** 서버 검색 전 로컬 검색어와 샘플 결과를 화면에 연결한다. */
@Composable
fun BookOnSearchRoute(
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    viewModel: BookOnSearchViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    BookOnSearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::search,
        onBackClick = onBackClick,
        onBookClick = onBookClick,
        onLoadMore = viewModel::loadNextPage,
        onRetry = {
            viewModel.search(uiState.query)
        },
    )
}
