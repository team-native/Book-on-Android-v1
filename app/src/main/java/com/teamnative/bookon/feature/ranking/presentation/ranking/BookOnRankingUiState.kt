package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingListUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingPodiumUiModel

@Immutable
data class BookOnRankingScreenUiState(
    val description: String,
    val podium: BookOnRankingPodiumUiModel,
    val list: BookOnRankingListUiModel,
)
