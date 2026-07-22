package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen

/** 대출 현황·이력 서버 상태와 필터 및 도서 상세 이동 이벤트를 화면에 연결한다. */
@Composable
fun BookOnLoanHistoryRoute(
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    viewModel: BookOnLoanHistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnLoanHistoryScreen(
            uiState = uiState,
            onEvent = { event ->
            when (event) {
                BookOnLoanHistoryScreenEvent.BackClicked -> onBackClick()
                is BookOnLoanHistoryScreenEvent.FilterClicked -> viewModel.selectFilter(event.filterIndex)
                is BookOnLoanHistoryScreenEvent.BookClicked -> onBookClick(event.bookId)
                BookOnLoanHistoryScreenEvent.RetryClicked -> viewModel.retry()
                BookOnLoanHistoryScreenEvent.LoadMoreClicked -> viewModel.loadMore()
            }
            },
        )
    }
}
