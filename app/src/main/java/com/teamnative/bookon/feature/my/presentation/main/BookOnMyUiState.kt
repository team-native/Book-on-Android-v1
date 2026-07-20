package com.teamnative.bookon.feature.my.presentation.main

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnMenuRowUiModel
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel
import com.teamnative.bookon.feature.my.presentation.model.BookOnMyMarathonUiModel

@Immutable
data class BookOnMyScreenUiState(
    val userNameText: String,
    val studentInfoText: String,
    val stats: List<BookOnStatItemUiModel>,
    val marathon: BookOnMyMarathonUiModel,
    val menus: List<BookOnMenuRowUiModel>,
)
