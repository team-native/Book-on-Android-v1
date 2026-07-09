package com.example.bookon.uiState.home

import androidx.compose.runtime.Immutable
import com.example.bookon.uiState.BookOnBookCardUiState

@Immutable
data class BookOnHomeScreenUiState(
    val greeting: String,
    val userName: String,
    val notice: BookOnHomeNoticeUiState,
    val aiRecommendationDescription: String,
    val aiRecommendedBooks: List<BookOnBookCardUiState>,
    val popularBooks: List<BookOnPopularBookRowUiState>,
    val newBooks: List<BookOnBookCardUiState>,
)

@Immutable
data class BookOnNewBooksScreenUiState(
    val books: List<BookOnBookCardUiState>,
)
