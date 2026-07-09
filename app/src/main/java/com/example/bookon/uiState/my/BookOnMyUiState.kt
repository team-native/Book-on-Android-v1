package com.example.bookon.uiState.my

import androidx.compose.runtime.Immutable
import com.example.bookon.ui.commonComponent.card.BookOnStatItem
import com.example.bookon.uiState.BookOnFilterChipUiState
import com.example.bookon.uiState.BookOnMenuRowUiState
import com.example.bookon.uiState.BookOnSwitchRowUiState
import com.example.bookon.uiState.book.BookOnBookListItemUiState

@Immutable
data class BookOnMyScreenUiState(
    val userNameText: String,
    val studentInfoText: String,
    val stats: List<BookOnStatItem>,
    val marathon: BookOnMyMarathonUiState,
    val menus: List<BookOnMenuRowUiState>,
    val notificationPanel: BookOnNotificationSettingsUiState,
)

@Immutable
data class BookOnMyMarathonUiState(
    val title: String,
    val statusText: String,
    val progressText: String,
    val remainingText: String,
    val percentText: String,
    val linked: Boolean,
    val progress: Float,
)

@Immutable
data class BookOnNotificationSettingsUiState(
    val title: String,
    val description: String,
    val rows: List<BookOnSwitchRowUiState>,
)

@Immutable
data class BookOnLoanHistoryScreenUiState(
    val filters: List<BookOnFilterChipUiState>,
    val currentTitle: String,
    val pastTitle: String,
    val currentLoans: List<BookOnBookListItemUiState>,
    val pastLoans: List<BookOnBookListItemUiState>,
)

@Immutable
data class BookOnFavoriteBooksScreenUiState(
    val summary: String,
    val books: List<BookOnBookListItemUiState>,
)
