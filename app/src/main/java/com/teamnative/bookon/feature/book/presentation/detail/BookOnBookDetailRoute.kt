package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.runtime.Composable

/** 대출 가능한 샘플 상세 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnBookDetailRoute(onBackClick: () -> Unit) {
    BookOnBookDetailScreen(
        uiState = sampleBookDetailUiState(loanAvailable = true),
        onBackClick = onBackClick,
        onLoanClick = {},
    )
}
