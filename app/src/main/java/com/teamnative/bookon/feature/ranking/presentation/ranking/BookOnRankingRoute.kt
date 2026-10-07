package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 랭킹 Route는 서버 랭킹 연동 전 샘플 랭킹 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnRankingRoute(
    bottomBar: @Composable () -> Unit,
    viewModel: BookOnRankingViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    BookOnRankingScreen(
        uiState = uiState,
        bottomBar = bottomBar,
        onRetryClick = viewModel::retry,
    )
}
