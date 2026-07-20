package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable



/**
 * 도서실 Route는 서버 목록 연동 전 카테고리와 정렬의 선택 인덱스를 관리해 Screen에 전달한다.
 */
@Composable
fun BookOnLibraryRoute(
    bottomBar: @Composable () -> Unit,
    onBookClick: () -> Unit,
) {
    var selectedCategoryIndex by rememberSaveable { mutableIntStateOf(ALL_CATEGORY_INDEX) }
    var selectedSortIndex by rememberSaveable { mutableIntStateOf(POPULAR_SORT_INDEX) }

    BookOnLibraryScreen(
        uiState = sampleLibraryUiState(
            selectedCategoryIndex = selectedCategoryIndex,
            selectedSortIndex = selectedSortIndex,
        ),
        bottomBar = bottomBar,
        onEvent = { event ->
            when (event) {
                is BookOnLibraryScreenEvent.CategoryClicked -> {
                    selectedCategoryIndex = event.categoryIndex
                }

                is BookOnLibraryScreenEvent.SortClicked -> {
                    selectedSortIndex = event.sortIndex
                }
            }
        },
        onBookClick = onBookClick,
    )
}

private const val ALL_CATEGORY_INDEX = 0
private const val POPULAR_SORT_INDEX = 0
