package com.teamnative.bookon.core.ui.component.loading

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookOnLoadingScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loadingScreen_displaysIndeterminateLoadingState() {
        val loadingDescription = composeRule.activity.getString(R.string.state_loading)

        composeRule.setContent {
            BookOnTheme {
                BookOnLoadingScreen()
            }
        }

        composeRule
            .onNode(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.StateDescription,
                    loadingDescription,
                ) and SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo.Indeterminate,
                ),
            )
            .assertIsDisplayed()
    }
}
