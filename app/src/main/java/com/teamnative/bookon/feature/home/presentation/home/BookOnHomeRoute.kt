package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.runtime.Composable

/** 서버 연동 전 홈 샘플 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnHomeRoute(
    bottomBar: @Composable () -> Unit,
    onSearchClick: () -> Unit,
    onNewBooksClick: () -> Unit,
    onNotificationClick: () -> Unit,
) {
    BookOnHomeScreen(
        uiState = sampleHomeUiState(),
        bottomBar = bottomBar,
        onSearchClick = onSearchClick,
        onShowMoreClick = onNewBooksClick,
        onNotificationClick = onNotificationClick,
    )
}
