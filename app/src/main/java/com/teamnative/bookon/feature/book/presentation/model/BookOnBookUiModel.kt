package com.teamnative.bookon.feature.book.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnBookDetailInfoItemUiModel(
    val label: String,
    val value: String,
    val highlighted: Boolean = false,
)

@Immutable
data class BookOnBookDetailInfoRowUiModel(
    val items: List<BookOnBookDetailInfoItemUiModel>,
)
