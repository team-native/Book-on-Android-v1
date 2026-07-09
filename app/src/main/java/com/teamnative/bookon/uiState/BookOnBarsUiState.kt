package com.teamnative.bookon.uiState

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.navigation.BookOnNavigationItem

@Immutable
data class BookOnTopBarUiState(
    val title: String,
    val backContentDescription: String = "뒤로가기",
)

@Immutable
data class BookOnStepProgressUiState(
    val currentStep: Int,
    val totalStep: Int,
)

@Immutable
data class BookOnBottomNavigationUiState(
    val items: List<BookOnNavigationItem>,
    val selectedIndex: Int,
)
