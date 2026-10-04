package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookOnBookDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoriteAndLoan_shareBottomRowAndKeepTheirActions() {
        var favoriteClicks = 0
        var loanClicks = 0
        composeTestRule.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    uiState = sampleBookDetailUiState(loanAvailable = true),
                    errorMessage = null,
                    onBackClick = {},
                    onLoanClick = { loanClicks++ },
                    onFavoriteClick = { favoriteClicks++ },
                )
            }
        }
        val favorite = composeTestRule.onNodeWithContentDescription("관심 도서로 추가")
        val loan = composeTestRule.onNodeWithText("대출 신청하기")
        val favoriteBounds = favorite.fetchSemanticsNode().boundsInRoot
        val loanBounds = loan.fetchSemanticsNode().boundsInRoot
        assertTrue(favoriteBounds.right < loanBounds.left)
        assertTrue(favoriteBounds.top <= loanBounds.center.y && favoriteBounds.bottom >= loanBounds.center.y)
        favorite.performClick()
        loan.performClick()
        composeTestRule.runOnIdle {
            assertEquals(1, favoriteClicks)
            assertEquals(1, loanClicks)
        }
    }

    @Test
    fun pendingMutation_disablesBothActions() {
        composeTestRule.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    uiState = sampleBookDetailUiState(loanAvailable = true).copy(isSubmitting = true),
                    errorMessage = null,
                    onBackClick = {},
                    onLoanClick = {},
                    onFavoriteClick = {},
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("관심 도서로 추가").assertIsNotEnabled()
        composeTestRule.onNodeWithText("대출 신청하기").assertIsNotEnabled()
    }
}
