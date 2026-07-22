package com.teamnative.bookon.feature.book.presentation.search

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

@Immutable
data class BookOnSearchScreenUiState(
    val query: String,
    val resultSummary: String,
    val books: List<BookOnBookListItemUiModel>,
    val emptyMessage: String,
    val isSearching: Boolean = false,
    val isPagingLoading: Boolean = false,
    val hasNext: Boolean = false,
    val errorMessage: String? = null,
)
