package com.teamnative.bookon.feature.auth.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnOptionButtonUiModel(
    val text: String,
    val selected: Boolean,
    val enabled: Boolean = true,
)

@Immutable
data class BookOnDropdownFieldUiModel(
    val text: String,
    val placeholder: String,
    val expanded: Boolean = false,
    val enabled: Boolean = true,
)
