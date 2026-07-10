package com.teamnative.bookon.feature.library.presentation.library

import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

internal fun sampleLibraryUiState() = BookOnLibraryScreenUiState(
    categories = listOf(
        BookOnFilterChipUiModel("전체", true),
        BookOnFilterChipUiModel("소설", false),
        BookOnFilterChipUiModel("과학", false),
        BookOnFilterChipUiModel("역사", false),
        BookOnFilterChipUiModel("개발", false),
    ),
    sortOptions = listOf(
        BookOnFilterChipUiModel("인기순", true),
        BookOnFilterChipUiModel("신간순", false),
    ),
    books = listOf(
        BookOnBookCardUiModel("프로젝트 헤일메리", "앤디 위어 · 재고 2권"),
        BookOnBookCardUiModel("괴테는 모든 것을 말했다", "스즈키 유이 · 재고 2권"),
        BookOnBookCardUiModel("인간 실격", "다자이 오사무 · 재고 2권"),
        BookOnBookCardUiModel("급류", "정대건 · 재고 2권"),
    ),
)
