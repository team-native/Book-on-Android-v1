package com.teamnative.bookon.feature.home.presentation.newbooks

import androidx.compose.runtime.Composable

/** 신간 추천 샘플 상태와 뒤로가기 이벤트를 연결한다. */
@Composable
fun BookOnNewBooksRoute(onBackClick: () -> Unit) {
    BookOnNewBooksScreen(
        uiState = sampleNewBooksUiState(),
        onBackClick = onBackClick,
    )
}
