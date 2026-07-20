package com.teamnative.bookon.feature.my.presentation.favorites

import androidx.compose.runtime.Composable

/** 즐겨찾기 샘플 상태와 도서 선택 이벤트를 연결한다. */
@Composable
fun BookOnFavoriteBooksRoute(
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    uiState: BookOnFavoriteBooksScreenUiState = sampleFavoriteBooksUiState(),
) {
    BookOnFavoriteBooksScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBookClick = onBookClick,
    )
}
