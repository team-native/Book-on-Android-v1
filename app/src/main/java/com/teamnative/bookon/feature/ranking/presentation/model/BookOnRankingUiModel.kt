package com.teamnative.bookon.feature.ranking.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class BookOnRankingMemberUiModel(
    val rank: Int,
    val name: String,
    val description: String,
    val bookCountText: String,
    @param:DrawableRes val avatarRes: Int? = null,
    val avatarContentDescription: String? = null,
)

@Immutable
data class BookOnRankingPodiumUiModel(
    val first: BookOnRankingMemberUiModel,
    val second: BookOnRankingMemberUiModel,
    val third: BookOnRankingMemberUiModel,
)

@Immutable
data class BookOnRankingListUiModel(
    val members: List<BookOnRankingMemberUiModel>,
)
