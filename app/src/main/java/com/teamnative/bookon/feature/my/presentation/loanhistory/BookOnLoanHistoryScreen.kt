package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.book.BookOnBookListItem
import com.teamnative.bookon.core.ui.component.chip.BookOnFilterChip
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator

/** 대출 중인 책과 지난 대출 목록을 필터와 함께 표시한다. */
@Composable
fun BookOnLoanHistoryScreen(
    uiState: BookOnLoanHistoryScreenUiState,
    onEvent: (BookOnLoanHistoryScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.loan_return_history),
                onBackClick = { onEvent(BookOnLoanHistoryScreenEvent.BackClicked) },
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
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                    uiState.filters.forEachIndexed { index, filter ->
                        BookOnFilterChip(
                            uiState = filter,
                            onClick = {
                                onEvent(BookOnLoanHistoryScreenEvent.FilterClicked(index))
                            },
                        )
                    }
                }
            }
            uiState.errorMessage?.let { errorMessage ->
                item {
                    Text(
                        text = errorMessage,
                        style = bookOnTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    TextButton(
                        onClick = {
                            onEvent(BookOnLoanHistoryScreenEvent.RetryClicked)
                        },
                    ) {
                        Text(text = stringResource(R.string.action_retry))
                    }
                }
            }
            if (uiState.errorMessage == null && uiState.currentLoans.isEmpty() && uiState.pastLoans.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.empty_loan_history),
                        style = bookOnTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (uiState.currentLoans.isNotEmpty()) {
                item {
                    SectionTitle(text = uiState.currentTitle)
                }
                items(uiState.currentLoans) { book ->
                    BookOnBookListItem(
                        uiState = book,
                        onClick = {
                            onEvent(BookOnLoanHistoryScreenEvent.BookClicked(book.id))
                        },
                    )
                }
            }
            if (uiState.pastLoans.isNotEmpty()) {
                item {
                    SectionTitle(text = uiState.pastTitle)
                }
                items(uiState.pastLoans) { book ->
                    BookOnBookListItem(
                        uiState = book,
                        onClick = {
                            onEvent(BookOnLoanHistoryScreenEvent.BookClicked(book.id))
                        },
                    )
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
                        onClick = {
                            onEvent(BookOnLoanHistoryScreenEvent.LoadMoreClicked)
                        },
                    ) {
                        Text(text = stringResource(R.string.action_load_more))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = bookOnTypography.bodySemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnLoanHistoryScreenPreview() {
    BookOnTheme {
        BookOnLoanHistoryScreen(
            uiState = sampleLoanHistoryUiState(),
            onEvent = {},
        )
    }
}
