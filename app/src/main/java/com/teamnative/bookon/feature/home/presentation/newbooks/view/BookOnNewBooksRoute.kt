package com.teamnative.bookon.feature.home.presentation.newbooks.view

import com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel.BookOnNewBooksViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel.BookOnNewBooksScreenEvent

/** 신간 목록 서버 상태와 뒤로가기 탐색을 연결한다. */
@Composable
fun BookOnNewBooksRoute(
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    viewModel: BookOnNewBooksViewModel = hiltViewModel(),
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
        )
    }
}
