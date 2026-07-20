package com.teamnative.bookon.feature.my.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnMyMarathonUiModel(
    val title: String,
    val statusText: String,
    val progressText: String,
    val remainingText: String,
    val percentText: String,
    val linked: Boolean,
    val progress: Float,
)
