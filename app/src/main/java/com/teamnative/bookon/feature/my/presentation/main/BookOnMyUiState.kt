package com.teamnative.bookon.feature.my.presentation.main

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnMenuRowUiModel
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel
import com.teamnative.bookon.feature.my.presentation.model.BookOnMyMarathonUiModel
import com.teamnative.bookon.feature.my.domain.NotificationSettings
import com.teamnative.bookon.core.ui.model.BookOnUiMessage

@Immutable
data class BookOnMyScreenUiState(
    val userNameText: String,
    val studentInfoText: String,
    val stats: List<BookOnStatItemUiModel>,
    val marathon: BookOnMyMarathonUiModel,
    val menus: List<BookOnMenuRowUiModel>,
    val notificationSettings: NotificationSettings = NotificationSettings(
        dueDateReminder = false,
        newBookReminder = false,
    ),
    val isReadingMarathonLinked: Boolean = false,
    val isInitialLoading: Boolean = false,
    val errorMessage: BookOnUiMessage? = null,
)
