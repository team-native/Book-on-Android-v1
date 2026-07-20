package com.teamnative.bookon.feature.book.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.book.BookOnBookListItem
import com.teamnative.bookon.feature.book.presentation.component.BookOnSearchBar

/** 검색 바, 결과 요약과 목록 또는 빈 상태를 표시한다. */
@Composable
fun BookOnSearchScreen(
    uiState: BookOnSearchScreenUiState,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.action_search),
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item {
                BookOnSearchBar(
                    query = uiState.query,
                    placeholder = stringResource(R.string.search_book_placeholder),
                    onQueryChange = onQueryChange,
                )
            }
            if (uiState.books.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppSpacing.Section),
                        text = uiState.emptyMessage,
                        style = BookOnTypography.bodySemiBold,
                        color = BookOnColor.TextSecondary,
                    )
                }
            } else {
                item {
                    Text(
                        text = uiState.resultSummary,
                        style = BookOnTypography.caption,
                        color = BookOnColor.TextSecondary,
                    )
                }
                items(uiState.books) { book ->
                    BookOnBookListItem(uiState = book, onClick = onBookClick)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSearchScreenPreview() {
    BookOnTheme {
        BookOnSearchScreen(
            uiState = sampleSearchUiState(),
            onQueryChange = {},
            onBackClick = {},
            onBookClick = {},
        )
    }
}
