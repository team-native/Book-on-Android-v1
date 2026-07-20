package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.runtime.Composable

/**
 * 랭킹 Route는 서버 랭킹 연동 전 샘플 랭킹 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnRankingRoute(bottomBar: @Composable () -> Unit) {
    BookOnRankingScreen(
        uiState = sampleRankingUiState(),
        bottomBar = bottomBar,
    )
}
