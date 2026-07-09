package com.example.bookon.ui.screen.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.R
import com.example.bookon.ui.commonComponent.card.BookOnBookCard
import com.example.bookon.ui.commonComponent.chip.BookOnFilterChip
import com.example.bookon.theme.AppComponentSize
import com.example.bookon.theme.AppElevation
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.BookOnBookCardUiState
import com.example.bookon.uiState.BookOnFilterChipUiState
import com.example.bookon.uiState.library.BookOnLibraryScreenUiState

/**
 * 도서실 Route는 서버 목록 연동 전 카테고리와 정렬 샘플 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnLibraryRoute(
    bottomBar: @Composable () -> Unit,
    onBookClick: () -> Unit,
) {
    BookOnLibraryScreen(
        uiState = previewLibraryUiState(),
        bottomBar = bottomBar,
        onCategoryClick = {},
        onSortClick = {},
        onBookClick = onBookClick,
    )
}

/**
 * 도서실 화면은 카테고리, 정렬 필터, 도서 목록을 표시한다.
 */
@Composable
fun BookOnLibraryScreen(
    uiState: BookOnLibraryScreenUiState,
    bottomBar: @Composable () -> Unit,
    onCategoryClick: (Int) -> Unit,
    onSortClick: (Int) -> Unit,
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
                    onSortClick = onSortClick,
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                    items(uiState.categories.size) { index ->
                        BookOnFilterChip(
                            uiState = uiState.categories[index],
                            onClick = { onCategoryClick(index) },
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

/**
 * 도서실 상단의 큰 제목과 인기순/신간순 정렬 세그먼트를 Figma 배치에 맞춰 표시한다.
 * 정렬 변경 이벤트는 화면 Route가 소유하고, 이 컴포넌트는 선택 상태만 표현한다.
 */
@Composable
private fun BookOnLibraryHeader(
    title: String,
    sortOptions: List<BookOnFilterChipUiState>,
    onSortClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = BookOnTypography.screenTitle,
            color = BookOnColor.TextPrimary,
        )
        Spacer(modifier = Modifier.weight(1f))
        BookOnLibrarySortToggle(
            sortOptions = sortOptions,
            onSortClick = onSortClick,
        )
    }
}

/**
 * Figma 도서실 화면 우측의 2분할 정렬 토글이다.
 * 선택 항목은 흰색 pill과 shadow로 강조하고, 비선택 항목은 보조 텍스트 색을 사용한다.
 */
@Composable
private fun BookOnLibrarySortToggle(
    sortOptions: List<BookOnFilterChipUiState>,
    onSortClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .width(AppComponentSize.LibrarySortToggleWidth)
            .height(AppComponentSize.LibrarySortToggleHeight)
            .clip(RoundedCornerShape(AppRadius.Button))
            .background(BookOnColor.SurfaceAlt)
            .padding(AppSpacing.Tiny),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Tiny),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        sortOptions.forEachIndexed { index, option ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(AppComponentSize.LibrarySortOptionHeight)
                    .clip(RoundedCornerShape(AppRadius.Button))
                    .then(
                        if (option.selected) {
                            Modifier
                                .shadow(
                                    elevation = AppElevation.Field,
                                    shape = RoundedCornerShape(AppRadius.Button),
                                )
                                .background(BookOnColor.Surface)
                        } else {
                            Modifier.background(BookOnColor.SurfaceAlt)
                        },
                    )
                    .clickable(role = Role.Button, onClick = { onSortClick(index) })
                    .padding(horizontal = AppSpacing.Small),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.text,
                    style = BookOnTypography.caption,
                    color = if (option.selected) {
                        BookOnColor.TextPrimary
                    } else {
                        BookOnColor.TextSecondary
                    },
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

private fun previewLibraryUiState() = BookOnLibraryScreenUiState(
    categories = listOf(
        BookOnFilterChipUiState("전체", true),
        BookOnFilterChipUiState("소설", false),
        BookOnFilterChipUiState("과학", false),
        BookOnFilterChipUiState("역사", false),
        BookOnFilterChipUiState("개발", false),
    ),
    sortOptions = listOf(
        BookOnFilterChipUiState("인기순", true),
        BookOnFilterChipUiState("신간순", false),
    ),
    books = listOf(
        BookOnBookCardUiState("프로젝트 헤일메리", "앤디 위어 · 재고 2권"),
        BookOnBookCardUiState("괴테는 모든 것을 말했다", "스즈키 유이 · 재고 2권"),
        BookOnBookCardUiState("인간 실격", "다자이 오사무 · 재고 2권"),
        BookOnBookCardUiState("급류", "정대건 · 재고 2권"),
    ),
)

@Preview(showBackground = true)
@Composable
private fun BookOnLibraryScreenPreview() {
    BookOnTheme {
        BookOnLibraryScreen(
            uiState = previewLibraryUiState(),
            bottomBar = {},
            onCategoryClick = {},
            onSortClick = {},
            onBookClick = {},
        )
    }
}
