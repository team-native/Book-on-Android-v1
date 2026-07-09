package com.teamnative.bookon.uiState.book

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnSearchScreenUiState(
    val query: String,
    val resultSummary: String,
    val books: List<BookOnBookListItemUiState>,
    val emptyMessage: String,
)

@Immutable
data class BookOnBookDetailScreenUiState(
    val title: String,
    val author: String,
    val info: BookOnBookDetailInfoRowUiState,
    val intro: String,
    val loanAvailable: Boolean,
)
