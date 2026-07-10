package com.teamnative.bookon.feature.home.presentation.newbooks

import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel

internal fun sampleNewBooksUiState() = BookOnNewBooksScreenUiState(
    books = listOf(
        BookOnBookCardUiModel("자몽 살구 클럽", "한로로"),
        BookOnBookCardUiModel("괴테는 모든 것을 말했다", "스즈키 유이"),
        BookOnBookCardUiModel("혼모노", "성해나"),
        BookOnBookCardUiModel("사랑하는 겉들", "이옥토"),
    ),
)
