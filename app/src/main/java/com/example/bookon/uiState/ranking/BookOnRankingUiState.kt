package com.example.bookon.uiState.ranking

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class BookOnRankingMemberUiState(
    val rank: Int,
    val name: String,
    val description: String,
    val bookCountText: String,
    @param:DrawableRes val avatarRes: Int? = null,
    val avatarContentDescription: String? = null,
)

@Immutable
data class BookOnRankingPodiumUiState(
    val first: BookOnRankingMemberUiState,
    val second: BookOnRankingMemberUiState,
    val third: BookOnRankingMemberUiState,
)

@Immutable
data class BookOnRankingListUiState(
    val members: List<BookOnRankingMemberUiState>,
)
