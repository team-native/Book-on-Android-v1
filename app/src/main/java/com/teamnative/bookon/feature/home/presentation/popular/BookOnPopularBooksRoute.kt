package com.teamnative.bookon.feature.home.presentation.popular

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel.BookOnNewBooksScreenEvent
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.feature.home.presentation.newbooks.view.BookOnNewBooksScreen

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
            onEvent = { event ->
                when (event) {
                    BookOnNewBooksScreenEvent.BackClicked -> onBackClick()
                    BookOnNewBooksScreenEvent.RetryClicked -> viewModel.retry()
                    BookOnNewBooksScreenEvent.LoadMoreClicked -> viewModel.loadNextPage()
                    is BookOnNewBooksScreenEvent.BookClicked -> onBookClick(event.bookId)
                }
            },
            titleRes = R.string.popular_books_school,
            emptyRes = R.string.empty_popular_books,
        )
    }
}
