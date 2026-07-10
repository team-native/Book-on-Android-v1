package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.teamnative.bookon.core.ui.component.chip.BookOnFilterChip

/** 대출 중인 책과 지난 대출 목록을 필터와 함께 표시한다. */
@Composable
fun BookOnLoanHistoryScreen(
    uiState: BookOnLoanHistoryScreenUiState,
    onBackClick: () -> Unit,
    onFilterClick: (Int) -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.loan_return_history),
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
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                    uiState.filters.forEachIndexed { index, filter ->
                        BookOnFilterChip(uiState = filter, onClick = { onFilterClick(index) })
                    }
                }
            }
            item { SectionTitle(text = uiState.currentTitle) }
            items(uiState.currentLoans) { book ->
                BookOnBookListItem(uiState = book, onClick = onBookClick)
            }
            item { SectionTitle(text = uiState.pastTitle) }
            items(uiState.pastLoans) { book ->
                BookOnBookListItem(uiState = book, onClick = onBookClick)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = BookOnTypography.bodySemiBold, color = BookOnColor.TextPrimary)
}

@Preview(showBackground = true)
@Composable
private fun BookOnLoanHistoryScreenPreview() {
    BookOnTheme {
        BookOnLoanHistoryScreen(
            uiState = sampleLoanHistoryUiState(),
            onBackClick = {},
            onFilterClick = {},
            onBookClick = {},
        )
    }
}
