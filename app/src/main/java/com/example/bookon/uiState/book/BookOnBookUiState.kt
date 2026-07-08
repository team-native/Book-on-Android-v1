package com.example.bookon.uiState.book

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnBookListItemUiState(
    val title: String,
    val metaText: String,
    val statusText: String? = null,
    val available: Boolean = true,
)

@Immutable
data class BookOnBookDetailInfoItemUiState(
    val label: String,
    val value: String,
    val highlighted: Boolean = false,
)

@Immutable
data class BookOnBookDetailInfoRowUiState(
    val items: List<BookOnBookDetailInfoItemUiState>,
)
