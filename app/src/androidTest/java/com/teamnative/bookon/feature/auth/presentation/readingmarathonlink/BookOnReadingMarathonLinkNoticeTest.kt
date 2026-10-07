package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import android.view.accessibility.AccessibilityEvent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.teamnative.bookon.MainActivity
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookOnReadingMarathonLinkNoticeTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun eachSocialButtonAnnouncesNotReadyWithoutNavigating() {
        var navigationCount = 0
        compose.activityRule.scenario.onActivity { activity ->
            activity.setContent {
                BookOnTheme {
                    BookOnReadingMarathonLinkRoute(
                        onBackClick = { navigationCount++ },
                        onSkipClick = { navigationCount++ },
                        onCompleteClick = { navigationCount++ },
                    )
                }
            }
        }
        compose.waitForIdle()
        val automation = InstrumentationRegistry.getInstrumentation().uiAutomation
        listOf("구글 계정 연동", "네이버 계정 연동", "카카오 계정 연동").forEach { description ->
            val noticeEvent = automation.executeAndWaitForEvent(
                { compose.onNodeWithContentDescription(description).performClick() },
                { event ->
                    event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED &&
                        event.text.any { it.toString() == "아직 준비 중입니다." }
                },
                5_000,
            )
            assertTrue(noticeEvent.text.any { it.toString() == "아직 준비 중입니다." })
            compose.runOnIdle { assertEquals(0, navigationCount) }
        }
    }
}
