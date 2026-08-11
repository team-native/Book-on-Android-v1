package com.teamnative.bookon.core.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnBookListItemUiModel(
    val title: String,
    val metaText: String,
    val statusText: String? = null,
    val available: Boolean = true,
    val isFavorite: Boolean = false,
    val coverImageUrl: String? = null,
    val id: Long = 0L,
)
