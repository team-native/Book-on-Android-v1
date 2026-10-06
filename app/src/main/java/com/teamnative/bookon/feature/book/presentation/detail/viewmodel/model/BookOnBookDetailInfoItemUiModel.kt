package com.teamnative.bookon.feature.book.presentation.detail.viewmodel.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnBookDetailInfoItemUiModel(
    val label: String,
    val value: String,
    val highlighted: Boolean = false,
)
