package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenUiState

@Composable
fun BookOnBookDetailActions(
    uiState: BookOnBookDetailScreenUiState,
    isRefreshing: Boolean,
    isLoanStateUnconfirmed: Boolean,
    onFavoriteClick: () -> Unit,
    onLoanClick: () -> Unit,
) {
    val isPending = uiState.isSubmitting || uiState.isFavoriteSubmitting || isRefreshing
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            BookOnPrimaryButton(
                text = stringResource(
                    if (uiState.isFavorite) {
                        R.string.favorite_remove_description
                    } else {
                        R.string.favorite_add_description
                    },
                ),
                onClick = onFavoriteClick,
                enabled = !isPending,
                loading = uiState.isFavoriteSubmitting,
                modifier = Modifier
                    .heightIn(min = AppComponentSize.BookDetailActionMinHeight)
                    .testTag("book_detail_favorite"),
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(AppRadius.LargeCard),
                textStyle = bookOnTypography.bookDetailAction,
                fixedHeight = null,
            )
            BookOnPrimaryButton(
                text = stringResource(
                    if (uiState.loanAvailable) {
                        R.string.loan_request
                    } else {
                        R.string.loan_unavailable
                    },
                ),
                onClick = onLoanClick,
                enabled = uiState.loanAvailable && !isPending && !isLoanStateUnconfirmed,
                loading = uiState.isSubmitting,
                modifier = Modifier
                    .heightIn(min = AppComponentSize.BookDetailActionMinHeight)
                    .testTag("book_detail_loan"),
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(AppRadius.LargeCard),
                textStyle = bookOnTypography.bookDetailAction,
                fixedHeight = null,
            )
        }
    }
}
