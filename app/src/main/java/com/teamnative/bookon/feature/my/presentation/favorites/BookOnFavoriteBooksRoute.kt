package com.teamnative.bookon.feature.my.presentation.favorites

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen

/** 즐겨찾기 샘플 상태와 도서 선택 이벤트를 연결한다. */
@Composable
fun BookOnFavoriteBooksRoute(
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    viewModel: BookOnFavoriteBooksViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnFavoriteBooksScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onBookClick = onBookClick,
            onRetryClick = viewModel::retry,
            onLoadMoreClick = viewModel::loadNextPage,
            onRemoveFavoriteClick = viewModel::removeFavorite,
        )
    }
}
