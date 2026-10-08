package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.core.ui.model.resolve

@Composable
fun BookOnBookDetailStatus(
    isLoading: Boolean,
    errorMessage: BookOnUiMessage?,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = AppSpacing.Content,
                alignment = Alignment.CenterVertically,
            ),
    ) {
        if (isLoading) {
            BookOnInlineLoadingIndicator()
        } else {
            Text(
                text = errorMessage?.resolve() ?: stringResource(R.string.book_detail_load_error),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onRetryClick) {
                Text(text = stringResource(R.string.action_retry))
            }
        }
    }
}
