package com.teamnative.bookon.feature.my.presentation.main

import androidx.compose.runtime.Composable

/** 내 서재 샘플 상태와 메뉴·로그아웃 이벤트를 연결한다. */
@Composable
fun BookOnMyRoute(
    bottomBar: @Composable () -> Unit,
    uiState: BookOnMyScreenUiState = sampleMyUiState(),
    onLoanHistoryClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLogoutRequest: () -> Unit,
) {
    BookOnMyScreen(
        uiState = uiState,
        bottomBar = bottomBar,
        onMenuClick = { index ->
            when (index) {
                1 -> onLoanHistoryClick()
                2 -> onFavoriteClick()
            }
        },
        onLogoutRequest = onLogoutRequest,
        onNotificationChanged = { _, _ -> },
    )
}
