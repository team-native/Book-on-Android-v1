package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.book.presentation.detail.view.BookOnBookDetailScreen
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenEvent
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailState
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.sampleBookDetailUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookOnBookDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoriteHeartKeepsItsActionWithoutLoanButton() {
        var favoriteClicks = 0
        composeTestRule.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state =
                        BookOnBookDetailState(
                            isInitialLoading = false,
                            content = sampleBookDetailUiState(loanAvailable = true),
                        ),
                    onEvent = { event ->
                        if (event == BookOnBookDetailScreenEvent.FavoriteClicked) {
                            favoriteClicks++
                        }
                    },
                )
            }
        }
        composeTestRule.onNodeWithTag("book_detail_favorite").performClick()
        composeTestRule.onNodeWithTag("book_detail_loan").assertDoesNotExist()
        composeTestRule.runOnIdle {
            assertEquals(1, favoriteClicks)
        }
    }

    @Test
    fun pendingFavoriteDisablesHeartWithoutLoanButton() {
        composeTestRule.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state =
                        BookOnBookDetailState(
                            isInitialLoading = false,
                            content =
                                sampleBookDetailUiState(loanAvailable = true).copy(
                                    isFavoriteSubmitting = true,
                                ),
                        ),
                    onEvent = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("book_detail_favorite").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("book_detail_loan").assertDoesNotExist()
    }
}
