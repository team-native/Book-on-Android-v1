package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

@Immutable
data class BookOnLibraryScreenUiState(
    val categories: List<BookOnFilterChipUiModel>,
    val sortOptions: List<BookOnFilterChipUiModel>,
    val books: List<BookOnBookCardUiModel>,
)
