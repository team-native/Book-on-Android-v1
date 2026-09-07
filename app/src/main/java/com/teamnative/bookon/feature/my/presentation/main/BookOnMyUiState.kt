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
    val profileImageUrl: String? = null,
    val stats: List<BookOnStatItemUiModel>,
    val marathon: BookOnMyMarathonUiModel,
    val menus: List<BookOnMenuRowUiModel>,
    val notificationSettings: NotificationSettings = NotificationSettings(
        dueDateReminder = false,
        newBookReminder = false,
    ),
    val isReadingMarathonLinked: Boolean = false,
    val isProfileImageUploading: Boolean = false,
    val isInitialLoading: Boolean = false,
    val isAccountDeletionInProgress: Boolean = false,
    val errorMessage: BookOnUiMessage? = null,
    val profileImageErrorMessage: BookOnUiMessage? = null,
    val accountDeletionErrorMessage: BookOnUiMessage? = null,
)

/** 서버 응답 전에는 Preview 샘플을 포함하지 않는 빈 화면 상태를 만든다. */
fun initialMyUiState() = BookOnMyScreenUiState(
    userNameText = "",
    studentInfoText = "",
    stats = emptyList(),
    marathon = BookOnMyMarathonUiModel(
        title = "",
        statusText = "",
        progressText = "",
        remainingText = "",
        percentText = "",
        linked = false,
        progress = 0f,
    ),
    menus = emptyList(),
    isInitialLoading = true,
)
