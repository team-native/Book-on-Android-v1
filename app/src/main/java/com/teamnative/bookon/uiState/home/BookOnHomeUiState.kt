package com.teamnative.bookon.uiState.home

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.teamnative.bookon.R

@Immutable
data class BookOnHomeSectionHeaderUiState(
    val title: String,
    val actionText: String? = null,
)

@Immutable
data class BookOnHomeNoticeUiState(
    val category: String,
    val dateText: String,
    val title: String,
    val description: String,
    val badgeText: String? = null,
    @param:DrawableRes val iconRes: Int = R.drawable.home_notification,
    val iconContentDescription: String? = null,
)

@Immutable
data class BookOnPopularBookRowUiState(
    val title: String,
    val metaText: String,
    val statusText: String? = null,
)
