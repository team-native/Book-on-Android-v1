package com.teamnative.bookon.uiState

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.teamnative.bookon.ui.Component.card.BookOnStatItem
import com.teamnative.bookon.theme.BookOnColor

@Immutable
data class BookOnStatSummaryCardUiState(
    val items: List<BookOnStatItem>,
)

@Immutable
data class BookOnInfoCardUiState(
    val title: String,
    val description: String? = null,
    val containerColor: Color = BookOnColor.Background,
    val borderColor: Color = BookOnColor.SurfaceBorder,
)

@Immutable
data class BookOnMenuRowUiState(
    val title: String,
    val destructive: Boolean = false,
    val showDivider: Boolean = true,
)

@Immutable
data class BookOnFilterChipUiState(
    val text: String,
    val selected: Boolean,
)

@Immutable
data class BookOnSwitchRowUiState(
    val title: String,
    val checked: Boolean,
    val description: String? = null,
)

@Immutable
data class BookOnBookCardUiState(
    val title: String,
    val author: String,
    val coverImageUrl: String? = null,
)
