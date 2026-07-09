package com.example.bookon.uiState.library

import androidx.compose.runtime.Immutable
import com.example.bookon.uiState.BookOnBookCardUiState
import com.example.bookon.uiState.BookOnFilterChipUiState

@Immutable
data class BookOnLibraryScreenUiState(
    val categories: List<BookOnFilterChipUiState>,
    val sortOptions: List<BookOnFilterChipUiState>,
    val books: List<BookOnBookCardUiState>,
)
