package com.teamnative.bookon.feature.library.presentation.library

import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

internal fun sampleLibraryUiState(
    selectedCategoryIndex: Int = ALL_CATEGORY_INDEX,
    selectedSortIndex: Int = POPULAR_SORT_INDEX,
) = BookOnLibraryScreenUiState(
    categories = categoryNames.mapIndexed { index, categoryName ->
        BookOnFilterChipUiModel(categoryName, selected = index == selectedCategoryIndex)
    },
    sortOptions = listOf("인기순", "신간순").mapIndexed { index, sortName ->
        BookOnFilterChipUiModel(sortName, selected = index == selectedSortIndex)
    },
    books = listOf(
        BookOnBookCardUiModel("프로젝트 헤일메리", "앤디 위어 · 재고 2권"),
        BookOnBookCardUiModel("괴테는 모든 것을 말했다", "스즈키 유이 · 재고 2권"),
        BookOnBookCardUiModel("인간 실격", "다자이 오사무 · 재고 2권"),
        BookOnBookCardUiModel("급류", "정대건 · 재고 2권"),
    ),
)

private val categoryNames = listOf("전체", "소설", "과학", "역사", "개발")

private const val ALL_CATEGORY_INDEX = 0
private const val POPULAR_SORT_INDEX = 0
