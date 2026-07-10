package com.teamnative.bookon.feature.my.presentation.favorites

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

@Immutable
data class BookOnFavoriteBooksScreenUiState(
    val summary: String,
    val books: List<BookOnBookListItemUiModel>,
)
