package com.teamnative.bookon.feature.home.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.teamnative.bookon.R

@Immutable
data class BookOnHomeNoticeUiModel(
    val category: String,
    val dateText: String,
    val title: String,
    val description: String,
    val badgeText: String? = null,
    @param:DrawableRes val iconRes: Int = R.drawable.home_notification,
    val iconContentDescription: String? = null,
)

@Immutable
data class BookOnPopularBookRowUiModel(
    val title: String,
    val metaText: String,
    val statusText: String? = null,
)
