package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.book.presentation.component.BookOnBookDetailCover
import com.teamnative.bookon.feature.book.presentation.component.BookOnBookDetailInfoRow

/** 도서 표지, 메타 정보, 소개와 대출 액션을 표시한다. */
@Composable
fun BookOnBookDetailScreen(
    uiState: BookOnBookDetailScreenUiState,
    onBackClick: () -> Unit,
    onLoanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = "",
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
        bottomBar = {
            BookOnPrimaryButton(
                text = stringResource(
                    if (uiState.loanAvailable) R.string.loan_request else R.string.loan_unavailable,
                ),
                onClick = onLoanClick,
                enabled = uiState.loanAvailable && !uiState.isSubmitting,
                modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(
                start = AppSpacing.ScreenHorizontal,
                end = AppSpacing.ScreenHorizontal,
                bottom = AppSpacing.Section,
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item { BookOnBookDetailCover() }
            item {
                Column {
                    Text(
                        text = uiState.title,
                        style = BookOnTypography.sectionTitle,
                        color = BookOnColor.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.Tiny))

                    Text(
                        text = uiState.author,
                        style = BookOnTypography.bodyMedium,
                        color = BookOnColor.TextSecondary,
                    )
                }
            }
            item { BookOnBookDetailInfoRow(uiState = uiState.info) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
                    Text(
                        text = stringResource(R.string.book_intro),
                        style = BookOnTypography.bodySemiBold,
                        color = BookOnColor.TextPrimary,
                    )
                    Text(
                        text = uiState.intro,
                        style = BookOnTypography.bodyMedium,
                        color = BookOnColor.TextSecondary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailScreenPreview() {
    BookOnTheme {
        BookOnBookDetailScreen(
            uiState = sampleBookDetailUiState(loanAvailable = true),
            onBackClick = {},
            onLoanClick = {},
        )
    }
}
