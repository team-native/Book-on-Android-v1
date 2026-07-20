package com.teamnative.bookon.feature.book.presentation.search

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

@Immutable
data class BookOnSearchScreenUiState(
    val query: String,
    val resultSummary: String,
    val books: List<BookOnBookListItemUiModel>,
    val emptyMessage: String,
)
