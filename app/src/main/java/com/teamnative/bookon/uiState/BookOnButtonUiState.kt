package com.teamnative.bookon.uiState

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnPrimaryButtonUiState(
    val text: String,
    val enabled: Boolean = true,
    val loading: Boolean = false,
)
