package com.teamnative.bookon.feature.book.presentation.search

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.book.BookOnBookListItem
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator
import com.teamnative.bookon.feature.book.presentation.component.BookOnSearchBar

/** 검색 바, 결과 요약과 목록 또는 빈 상태를 표시한다. */
@Composable
fun BookOnSearchScreen(
    uiState: BookOnSearchScreenUiState,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.action_search),
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.ScreenHorizontal),
        ) {
            BookOnSearchBar(
                query = uiState.query,
                placeholder = stringResource(R.string.search_book_placeholder),
                onQueryChange = onQueryChange,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Content))

            if (uiState.isEmptySearchResult()) {
                SearchEmptyResult(
                    modifier = Modifier.weight(1f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
                ) {
                    if (uiState.isSearching && uiState.books.isEmpty()) {
                        item { BookOnInlineLoadingIndicator() }
                    } else if (uiState.errorMessage != null && uiState.books.isEmpty()) {
                        item {
                            Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Button(modifier = Modifier.fillMaxWidth(), onClick = onRetry) {
                                Text(text = stringResource(R.string.action_retry))
                            }
                        }
                    } else if (uiState.books.isEmpty()) {
                        item {
                            Text(
                                text = uiState.emptyMessage,
                                style = bookOnTypography.bodySemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = uiState.resultSummary,
                                style = bookOnTypography.caption,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        items(uiState.books) { book ->
                            BookOnBookListItem(uiState = book, onClick = { onBookClick(book.id) })
                        }
                        if (uiState.isPagingLoading) {
                            item { BookOnInlineLoadingIndicator() }
                        }
                        uiState.errorMessage?.let { message ->
                            item {
                                Text(text = message, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Button(modifier = Modifier.fillMaxWidth(), onClick = onRetry) {
                                    Text(text = stringResource(R.string.action_retry))
                                }
                            }
                        }
                        if (uiState.hasNext && !uiState.isPagingLoading) {
                            item {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = onLoadMore,
                                ) {
                                    Text(text = stringResource(R.string.action_load_more))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 서버 검색이 완료됐지만 결과가 없을 때 보여 주는 안내 영역이다. */
@Composable
private fun SearchEmptyResult(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.search_noting_book),
                contentDescription = null,
                modifier = Modifier.size(
                    width = 80.dp,
                    height = 60.dp,
                ),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            Text(
                text = stringResource(R.string.empty_search_result),
                style = bookOnTypography.bodySemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun BookOnSearchScreenUiState.isEmptySearchResult(): Boolean {
    return query.isNotBlank() &&
        !isSearching &&
        !isPagingLoading &&
        errorMessage == null &&
        books.isEmpty()
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
            onLoadMore = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSearchEmptyResultPreview() {
    BookOnTheme {
        BookOnSearchScreen(
            uiState = sampleSearchUiState(empty = true),
            onQueryChange = {},
            onBackClick = {},
            onBookClick = {},
            onLoadMore = {},
            onRetry = {},
        )
    }
}
