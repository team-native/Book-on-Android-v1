package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

@Immutable
data class BookOnLoanHistoryScreenUiState(
    val filters: List<BookOnFilterChipUiModel>,
    val currentTitle: String,
    val pastTitle: String,
    val currentLoans: List<BookOnBookListItemUiModel>,
    val pastLoans: List<BookOnBookListItemUiModel>,
    val isInitialLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasNext: Boolean = false,
    val isPagingLoading: Boolean = false,
)
