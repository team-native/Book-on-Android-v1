package com.teamnative.bookon.feature.home.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookOnHomeScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeBooks_sendSelectedBookIdAndOnlyPopularSectionShowsMoreAction() {
        var latestEvent: BookOnHomeScreenEvent? = null
        val showMoreText = composeRule.activity.getString(R.string.action_show_more)

        composeRule.setContent {
            BookOnTheme {
                BookOnHomeScreen(
                    uiState = homeUiState(),
                    bottomBar = {},
                    onEvent = { event ->
                        latestEvent = event
                    },
                )
            }
        }

        composeRule
            .onNodeWithText("AI 첫 번째")
            .performScrollTo()
            .performClick()

        composeRule.runOnIdle {
            assertEquals(
                BookOnHomeScreenEvent.BookClicked(11L),
                latestEvent,
            )
        }

        composeRule
            .onNodeWithText("인기 첫 번째")
            .performScrollTo()
            .performClick()

        composeRule.runOnIdle {
            assertEquals(
                BookOnHomeScreenEvent.BookClicked(21L),
                latestEvent,
            )
        }

        composeRule
            .onAllNodesWithText(showMoreText)
            .assertCountEquals(1)
    }

    private fun homeUiState() = BookOnHomeScreenUiState(
        greeting = "좋은 오후예요",
        userName = "홍길동",
        notice = null,
        aiRecommendationDescription = "학교 대출 통계 기반 추천",
        aiRecommendedBooks = listOf(
            BookOnBookCardUiModel(
                title = "AI 첫 번째",
                author = "추천 작가",
                id = 11L,
            ),
            BookOnBookCardUiModel(
                title = "AI 두 번째",
                author = "추천 작가",
                id = 12L,
            ),
        ),
        popularBooks = listOf(
            BookOnPopularBookRowUiModel(
                id = 21L,
                title = "인기 첫 번째",
                metaText = "인기 작가 · 대출가능",
            ),
        ),
        newBooks = emptyList(),
    )
}
