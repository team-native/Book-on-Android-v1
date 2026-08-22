package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookOnNotificationSettingsBottomSheetContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationSwitches_updateTheirOwnSelection() {
        composeTestRule.setContent {
            var selections by remember { mutableStateOf(listOf(true, true, false)) }

            BookOnTheme {
                BookOnNotificationSettingsBottomSheetContent(
                    notificationSelections = selections,
                    onCheckedChange = { index, checked ->
                        selections = selections.mapIndexed { selectionIndex, selected ->
                            if (selectionIndex == index) checked else selected
                        }
                    },
                    onCompleteClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("notification_switch_0").assertIsOn()
        composeTestRule.onNodeWithTag("notification_setting_row_1").assertHasNoClickAction()
        composeTestRule.onNodeWithTag("notification_switch_0").performClick()
        composeTestRule.onNodeWithTag("notification_switch_0").assertIsOff()
        composeTestRule.onNodeWithTag("notification_switch_1").assertIsOn()
        composeTestRule.onNodeWithTag("notification_switch_2").assertIsOff()
        composeTestRule.onNodeWithTag("notification_switch_1").performClick()
        composeTestRule.onNodeWithTag("notification_switch_1").assertIsOff()
        composeTestRule.onNodeWithTag("notification_switch_0").assertIsOff()
        composeTestRule.onNodeWithTag("notification_switch_2").performClick()
        composeTestRule.onNodeWithTag("notification_switch_2").assertIsOn()
    }

    @Test
    fun completeButton_invokesCompleteCallback() {
        var completeClickCount = 0

        composeTestRule.setContent {
            BookOnTheme {
                BookOnNotificationSettingsBottomSheetContent(
                    notificationSelections = listOf(true, true, false),
                    onCheckedChange = { _, _ -> },
                    onCompleteClick = { completeClickCount++ },
                )
            }
        }

        composeTestRule.onNodeWithTag("notification_complete_button").performClick()

        composeTestRule.runOnIdle {
            assertEquals(1, completeClickCount)
        }
    }
}
