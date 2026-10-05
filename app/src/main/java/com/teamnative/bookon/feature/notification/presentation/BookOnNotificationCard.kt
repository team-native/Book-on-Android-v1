package com.teamnative.bookon.feature.notification.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.feature.notification.domain.BookOnNotification
import com.teamnative.bookon.feature.notification.domain.NotificationKind

@Composable
fun BookOnNotificationCard(
    notification: BookOnNotification,
    isExpanded: Boolean,
    isMarking: Boolean,
    onEvent: (BookOnNotificationsScreenEvent) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppSpacing.Content),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
        ) {
            TextButton(onClick = { onEvent(BookOnNotificationsScreenEvent.Expand(notification.id)) }) {
                Text(notification.title, style = MaterialTheme.typography.titleMedium)
            }
            Text(notification.createdAt, style = MaterialTheme.typography.labelMedium)
            Text(stringResource(if (notification.isRead) R.string.notifications_read else R.string.notifications_unread))
            if (isExpanded) {
                Text(notification.body, style = MaterialTheme.typography.bodyLarge)
                if (!notification.isRead && !isMarking) {
                    TextButton(onClick = { onEvent(BookOnNotificationsScreenEvent.RetryRead(notification.id)) }) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
                val canOpen = when (notification.kind) {
                    NotificationKind.LoanDue, NotificationKind.Notice -> true
                    NotificationKind.NewBook -> notification.bookId != null
                    NotificationKind.Unknown -> false
                }
                if (canOpen) {
                    TextButton(onClick = { onEvent(BookOnNotificationsScreenEvent.Related(notification)) }) {
                        Text(stringResource(R.string.notifications_related))
                    }
                }
            }
        }
    }
}
