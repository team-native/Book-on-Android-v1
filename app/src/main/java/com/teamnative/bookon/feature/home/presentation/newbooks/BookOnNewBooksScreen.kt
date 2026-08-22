package com.teamnative.bookon.feature.home.presentation.newbooks

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.card.BookOnBookCard
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator
import com.teamnative.bookon.core.ui.model.resolve

/** 신간 추천 책을 두 열 카드 목록으로 표시한다. */
@Composable
fun BookOnNewBooksScreen(
    uiState: BookOnNewBooksScreenUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onLoadMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.new_books_recent),
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
            uiState.errorMessage?.let { message ->
                item {
                    Text(
                        text = message.resolve(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Button(onClick = onRetryClick) {
                        Text(text = stringResource(R.string.action_retry))
                    }
                }
            }
            if (uiState.errorMessage == null && uiState.books.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.empty_new_books),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(uiState.books.chunked(2)) { rowBooks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    rowBooks.forEach { book ->
                        BookOnBookCard(uiState = book)
                    }

                    if (rowBooks.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            if (uiState.isPagingLoading) {
                item {
                    BookOnInlineLoadingIndicator()
                }
            }
            if (uiState.hasNext && !uiState.isPagingLoading) {
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onLoadMoreClick,
                    ) {
                        Text(text = stringResource(R.string.action_load_more))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnNewBooksScreenPreview() {
    BookOnTheme {
        BookOnNewBooksScreen(
            uiState = sampleNewBooksUiState(),
            onBackClick = {},
            onRetryClick = {},
            onLoadMoreClick = {},
        )
    }
}
