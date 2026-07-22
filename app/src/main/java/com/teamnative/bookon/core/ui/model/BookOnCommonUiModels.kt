package com.teamnative.bookon.core.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnMenuRowUiModel(
    val title: String,
    val destructive: Boolean = false,
    val showDivider: Boolean = true,
)

@Immutable
data class BookOnFilterChipUiModel(
    val text: String,
    val selected: Boolean,
)

@Immutable
data class BookOnSwitchRowUiModel(
    val title: String,
    val checked: Boolean,
    val description: String? = null,
)

@Immutable
data class BookOnBookCardUiModel(
    val title: String,
    val author: String,
    val coverImageUrl: String? = null,
    val id: Long = 0L,
)
