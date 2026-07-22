package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen

/**
 * 랭킹 Route는 서버 랭킹 연동 전 샘플 랭킹 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnRankingRoute(
    bottomBar: @Composable () -> Unit,
    viewModel: BookOnRankingViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnRankingScreen(
            uiState = uiState,
            bottomBar = bottomBar,
            onRetryClick = viewModel::retry,
        )
    }
}
