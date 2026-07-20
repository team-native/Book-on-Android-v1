package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.component.card.BookOnBookCard
import com.teamnative.bookon.core.ui.component.chip.BookOnFilterChip
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.library.presentation.component.BookOnLibraryHeader

/**
 * 도서실 화면은 카테고리, 정렬 필터, 도서 목록을 표시한다.
 */
@Composable
fun BookOnLibraryScreen(
    uiState: BookOnLibraryScreenUiState,
    bottomBar: @Composable () -> Unit,
    onEvent: (BookOnLibraryScreenEvent) -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = BookOnColor.Surface,
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(
                start = AppSpacing.ScreenHorizontal,
                top = AppSpacing.Content,
                end = AppSpacing.Content,
                bottom = AppSpacing.Section,
            ),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                BookOnLibraryHeader(
                    title = stringResource(R.string.nav_library),
                    sortOptions = uiState.sortOptions,
                    onSortClick = { sortIndex ->
                        onEvent(BookOnLibraryScreenEvent.SortClicked(sortIndex))
                    },
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                    items(uiState.categories.size) { index ->
                        BookOnFilterChip(
                            uiState = uiState.categories[index],
                            onClick = {
                                onEvent(BookOnLibraryScreenEvent.CategoryClicked(index))
                            },
                        )
                    }
                }
            }
            items(uiState.books) { book ->
                BookOnBookCard(
                    uiState = book,
                    coverWidth = AppComponentSize.LibraryBookCoverWidth,
                    coverHeight = AppComponentSize.LibraryBookCoverHeight,
                    cardWidth = AppComponentSize.LibraryBookCoverWidth,
                    cover = { LibraryBookCoverPlaceholder() },
                    modifier = Modifier.clickable(role = Role.Button, onClick = onBookClick),
                )
            }
        }
    }
}

@Composable
private fun LibraryBookCoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BookOnColor.BookCoverSmallPlaceholder),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnLibraryScreenPreview() {
    BookOnTheme {
        BookOnLibraryScreen(
            uiState = sampleLibraryUiState(),
            bottomBar = {},
            onEvent = {},
            onBookClick = {},
        )
    }
}
