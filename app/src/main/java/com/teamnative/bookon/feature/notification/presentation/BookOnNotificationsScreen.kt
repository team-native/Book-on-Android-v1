package com.teamnative.bookon.feature.notification.presentation

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
import com.teamnative.bookon.feature.notification.domain.NotificationKind

@Composable
fun BookOnNotificationsScreen(
    uiState: BookOnNotificationsUiState,
    onEvent: (BookOnNotificationsScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.notifications_title),
                onBackClick = { onEvent(BookOnNotificationsScreenEvent.Back) },
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
                Button(
                    onClick = { onEvent(BookOnNotificationsScreenEvent.ReadAll) },
                    enabled = !uiState.isMarkingAll && uiState.markingIds.isEmpty() && uiState.notifications.isNotEmpty(),
                ) {
                    Text(stringResource(R.string.notifications_read_all))
                }
            }
            if (uiState.hasError) {
                item {
                    Text(stringResource(R.string.notifications_error))
                    Button(onClick = { onEvent(BookOnNotificationsScreenEvent.Retry) }) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
            }
            if (!uiState.isLoading && !uiState.hasError && uiState.notifications.isEmpty()) {
                item { Text(stringResource(R.string.notifications_empty)) }
            }
            items(uiState.notifications, key = { it.id }) { notification ->
                BookOnNotificationCard(
                    notification = notification,
                    isExpanded = notification.id == uiState.expandedId,
                    isMarking = notification.id in uiState.markingIds || uiState.isMarkingAll,
                    onEvent = onEvent,
                )
            }
            if (uiState.isLoading || uiState.isMarkingAll) {
                item { BookOnInlineLoadingIndicator() }
            } else if (uiState.hasNext && !uiState.hasError) {
                item {
                    Button(onClick = { onEvent(BookOnNotificationsScreenEvent.LoadMore) }) {
                        Text(stringResource(R.string.action_load_more))
                    }
                }
            }
        }
    }
}
