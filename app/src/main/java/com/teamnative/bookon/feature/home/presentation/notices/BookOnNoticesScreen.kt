package com.teamnative.bookon.feature.home.presentation.notices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator

@Composable
fun BookOnNoticesScreen(
    uiState: BookOnNoticesUiState,
    onEvent: (BookOnNoticesScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.notices_title),
                onBackClick = { onEvent(BookOnNoticesScreenEvent.Back) },
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            if (uiState.hasError) {
                item {
                    Text(stringResource(R.string.notices_error))
                    Button(onClick = { onEvent(BookOnNoticesScreenEvent.Retry) }) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
            }
            if (!uiState.isLoading && !uiState.hasError && uiState.notices.isEmpty()) {
                item { Text(stringResource(R.string.notices_empty)) }
            }
            items(uiState.notices, key = { it.noticeId }) { notice ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        onEvent(BookOnNoticesScreenEvent.Open(notice))
                    },
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.Content),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
                    ) {
                        Text(notice.title, style = MaterialTheme.typography.titleMedium)
                        Text(notice.createdAt, style = MaterialTheme.typography.labelMedium)
                        Text(notice.summary, maxLines = 3)
                    }
                }
            }
            if (uiState.isLoading) {
                item { BookOnInlineLoadingIndicator() }
            } else if (uiState.hasNext && !uiState.hasError) {
                item {
                    Button(onClick = { onEvent(BookOnNoticesScreenEvent.LoadMore) }) {
                        Text(stringResource(R.string.action_load_more))
                    }
                }
            }
        }
    }
}
