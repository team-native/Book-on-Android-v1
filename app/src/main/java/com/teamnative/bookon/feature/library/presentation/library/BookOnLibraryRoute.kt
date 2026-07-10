package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.runtime.Composable



/**
 * 도서실 Route는 서버 목록 연동 전 카테고리와 정렬 샘플 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnLibraryRoute(
    bottomBar: @Composable () -> Unit,
    onBookClick: () -> Unit,
) {
    BookOnLibraryScreen(
        uiState = sampleLibraryUiState(),
        bottomBar = bottomBar,
        onCategoryClick = {},
        onSortClick = {},
        onBookClick = onBookClick,
    )
}
