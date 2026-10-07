package com.teamnative.bookon.feature.notification.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.notification.domain.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class BookOnNotificationCardTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun failedReadRetryDoesNotSendExpand() {
        var selectedEvent: BookOnNotificationsScreenEvent? = null
        compose.setContent {
            BookOnTheme {
                BookOnNotificationCard(
                    notification = BookOnNotification(7, NotificationKind.Unknown, "title", "body", false, "date", null),
                    isExpanded = true,
                    isMarking = false,
                    onEvent = { selectedEvent = it },
                )
            }
        }
        compose.onNodeWithText("다시 시도").performClick()
        compose.runOnIdle { assertEquals(BookOnNotificationsScreenEvent.RetryRead(7), selectedEvent) }
        compose.onNodeWithText("body").assertIsDisplayed()
        compose.onNodeWithText("관련 화면 열기").assertDoesNotExist()
    }
}
