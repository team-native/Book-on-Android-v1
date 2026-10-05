package com.teamnative.bookon.feature.home.presentation.popular

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.feature.home.presentation.newbooks.BookOnNewBooksScreen

@Composable
fun BookOnPopularBooksRoute(
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    viewModel: BookOnPopularBooksViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnNewBooksScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onBookClick = onBookClick,
            onRetryClick = viewModel::retry,
            onLoadMoreClick = viewModel::loadNextPage,
            titleRes = R.string.popular_books_school,
            emptyRes = R.string.empty_popular_books,
        )
    }
}
