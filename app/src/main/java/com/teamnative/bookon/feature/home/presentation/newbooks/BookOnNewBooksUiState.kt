package com.teamnative.bookon.feature.home.presentation.newbooks

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel

@Immutable
data class BookOnNewBooksScreenUiState(
    val books: List<BookOnBookCardUiModel>,
)
