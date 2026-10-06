package com.teamnative.bookon.feature.book.presentation.detail.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnBookDetailScreenUiState(
    val title: String,
    val author: String,
    val coverImageUrl: String? = null,
    val libraryNumber: String,
    val totalQuantity: Int? = null,
    val availableQuantity: Int? = null,
    val intro: String,
    val loanAvailable: Boolean,
    val isFavorite: Boolean = false,
    val isSubmitting: Boolean = false,
    val isFavoriteSubmitting: Boolean = false,
)
