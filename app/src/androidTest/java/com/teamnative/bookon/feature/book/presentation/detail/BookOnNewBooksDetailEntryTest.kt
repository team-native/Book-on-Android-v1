package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.newbooks.view.BookOnNewBooksScreen
import com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel.BookOnNewBooksScreenEvent
import com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel.BookOnNewBooksScreenUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookOnNewBooksDetailEntryTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun newBooksClickPreservesLongBookId() {
        var clickedId: Long? = null
        compose.setContent {
            BookOnTheme {
                BookOnNewBooksScreen(
                    uiState = BookOnNewBooksScreenUiState(
                        books = listOf(BookOnBookCardUiModel("테스트 책", "저자", id = 8013595087L)),
                    ),
                    onEvent = {
                        if (it is BookOnNewBooksScreenEvent.BookClicked) {
                            clickedId = it.bookId
                        }
                    },
                )
            }
        }
        compose.onNode(hasText("테스트 책") and hasClickAction()).performClick()
        compose.runOnIdle { assertEquals(8013595087L, clickedId) }
    }

}
