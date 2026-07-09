package com.teamnative.bookon.uiState.library

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.uiState.BookOnBookCardUiState
import com.teamnative.bookon.uiState.BookOnFilterChipUiState

@Immutable
data class BookOnLibraryScreenUiState(
    val categories: List<BookOnFilterChipUiState>,
    val sortOptions: List<BookOnFilterChipUiState>,
    val books: List<BookOnBookCardUiState>,
)
