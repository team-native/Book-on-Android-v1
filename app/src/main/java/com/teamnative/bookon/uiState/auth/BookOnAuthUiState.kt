package com.teamnative.bookon.uiState.auth

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnOptionButtonUiState(
    val text: String,
    val selected: Boolean,
    val enabled: Boolean = true,
)

@Immutable
data class BookOnDropdownFieldUiState(
    val text: String,
    val placeholder: String,
    val expanded: Boolean = false,
    val enabled: Boolean = true,
)
