package com.teamnative.bookon.feature.my.presentation.favorites

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage

@Immutable
data class BookOnFavoriteBooksScreenUiState(
    val summary: String,
    val books: List<BookOnBookListItemUiModel>,
    val isInitialLoading: Boolean = false,
    val isPagingLoading: Boolean = false,
    val errorMessage: BookOnUiMessage? = null,
    val hasNext: Boolean = false,
)
