package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.book.presentation.detail.view.BookOnBookDetailScreen
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenEvent
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookOnDetailBackTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun initialLoadingKeepsBackButton() {
        var event: BookOnBookDetailScreenEvent? = null
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(),
                    onEvent = { event = it },
                )
            }
        }
        compose.onNode(hasClickAction()).performClick()
        compose.runOnIdle { assertEquals(BookOnBookDetailScreenEvent.BackClicked, event) }
    }
}
