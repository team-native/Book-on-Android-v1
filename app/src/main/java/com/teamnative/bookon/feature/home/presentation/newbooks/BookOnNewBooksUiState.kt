package com.teamnative.bookon.feature.home.presentation.newbooks

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage

@Immutable
data class BookOnNewBooksScreenUiState(
    val books: List<BookOnBookCardUiModel>,
    val isInitialLoading: Boolean = false,
    val isPagingLoading: Boolean = false,
    val errorMessage: BookOnUiMessage? = null,
    val hasNext: Boolean = false,
)
