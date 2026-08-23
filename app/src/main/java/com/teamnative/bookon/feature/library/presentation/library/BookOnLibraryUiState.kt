package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage

@Immutable
data class BookOnLibraryScreenUiState(
    val categories: List<BookOnLibraryCategoryUiModel>,
    val sortOptions: List<BookOnFilterChipUiModel>,
    val books: List<BookOnBookCardUiModel>,
    val isInitialLoading: Boolean = false,
    val isPagingLoading: Boolean = false,
    val errorMessage: BookOnUiMessage? = null,
    val hasNext: Boolean = false,
)
