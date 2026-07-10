package com.teamnative.bookon.feature.my.presentation.model

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnSwitchRowUiModel

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

@Immutable
data class BookOnNotificationSettingsUiModel(
    val title: String,
    val description: String,
    val rows: List<BookOnSwitchRowUiModel>,
)
