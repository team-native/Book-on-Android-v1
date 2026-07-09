package com.teamnative.bookon.uiState.ranking

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnRankingScreenUiState(
    val description: String,
    val podium: BookOnRankingPodiumUiState,
    val list: BookOnRankingListUiState,
)
