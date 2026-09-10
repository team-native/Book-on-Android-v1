package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookOnHomeBookSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun aiRecommendationCard_invokesBookClickWithBookId() {
        var clickedBookId: Long? = null

        composeTestRule.setContent {
            BookOnTheme {
                BookOnBookSection(
                    title = "AI 추천",
                    books = listOf(
                        BookOnBookCardUiModel(
                            title = "소프트웨어 공학",
                            author = "저자",
                            id = 8013595087L,
                        ),
                    ),
                    onBookClick = { bookId -> clickedBookId = bookId },
                )
            }
        }

        composeTestRule.onNode(hasClickAction()).performClick()

        composeTestRule.runOnIdle {
            assertEquals(8013595087L, clickedBookId)
        }
    }

    @Test
    fun popularBookCard_invokesBookClickWithBookId() {
        var clickedBookId: Long? = null

        composeTestRule.setContent {
            BookOnTheme {
                BookOnPopularBooksSection(
                    title = "우리 학교 인기 책",
                    books = listOf(
                        BookOnPopularBookRowUiModel(
                            id = 8013595088L,
                            title = "데미안",
                            metaText = "헤르만 헤세 · 대출가능",
                        ),
                    ),
                    onBookClick = { bookId -> clickedBookId = bookId },
                )
            }
        }

        composeTestRule.onNode(hasClickAction()).performClick()

        composeTestRule.runOnIdle {
            assertEquals(8013595088L, clickedBookId)
        }
    }
}
