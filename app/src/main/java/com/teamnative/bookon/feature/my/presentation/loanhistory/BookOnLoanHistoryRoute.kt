package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.runtime.Composable

/** 대출·반납 샘플 상태와 필터·도서 선택 이벤트를 연결한다. */
@Composable
fun BookOnLoanHistoryRoute(
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    uiState: BookOnLoanHistoryScreenUiState = sampleLoanHistoryUiState(),
) {
    BookOnLoanHistoryScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFilterClick = {},
        onBookClick = onBookClick,
    )
}
