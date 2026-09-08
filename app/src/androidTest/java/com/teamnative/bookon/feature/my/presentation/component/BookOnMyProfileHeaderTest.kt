package com.teamnative.bookon.feature.my.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookOnMyProfileHeaderTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun profileEditButton_clicksVisibleBadgeAction() {
        var clickCount = 0
        val editContentDescription = composeRule.activity.getString(
            R.string.profile_image_edit_description,
        )

        composeRule.setContent {
            BookOnTheme {
                BookOnMyProfileHeader(
                    userNameText = "홍길동 님",
                    studentInfoText = "10기 · 소프트웨어 개발과",
                    profileImageUrl = null,
                    isProfileImageUploading = false,
                    onProfileImageEditClick = {
                        clickCount += 1
                    },
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(editContentDescription)
            .assertIsDisplayed()
            .performClick()

        composeRule.runOnIdle {
            assertEquals(1, clickCount)
        }
    }

    @Test
    fun profileEditButton_disablesClickWhileUploading() {
        val editContentDescription = composeRule.activity.getString(
            R.string.profile_image_edit_description,
        )

        composeRule.setContent {
            BookOnTheme {
                BookOnMyProfileHeader(
                    userNameText = "홍길동 님",
                    studentInfoText = "10기 · 소프트웨어 개발과",
                    profileImageUrl = null,
                    isProfileImageUploading = true,
                    onProfileImageEditClick = {},
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(editContentDescription)
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }
}
