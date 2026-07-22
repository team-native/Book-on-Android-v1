package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingListUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingPodiumUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage

@Immutable
data class BookOnRankingScreenUiState(
    val description: String,
    val podium: BookOnRankingPodiumUiModel,
    val list: BookOnRankingListUiModel,
    val isInitialLoading: Boolean = false,
    val errorMessage: BookOnUiMessage? = null,
)
