package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoRowUiModel

@Immutable
data class BookOnBookDetailScreenUiState(
    val title: String,
    val author: String,
    val coverImageUrl: String? = null,
    val info: BookOnBookDetailInfoRowUiModel,
    val intro: String,
    val loanAvailable: Boolean,
    val isSubmitting: Boolean = false,
)
