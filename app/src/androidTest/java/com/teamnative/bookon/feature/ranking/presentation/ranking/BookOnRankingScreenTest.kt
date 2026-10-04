package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingListUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingMemberUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingPodiumUiModel
import org.junit.Rule
import org.junit.Test

class BookOnRankingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyMessage_dependsOnAllReadersInsteadOfFourthPlace() {
        val readerCount = mutableIntStateOf(0)
        composeTestRule.setContent {
            val members = (1..readerCount.intValue).map { rank ->
                BookOnRankingMemberUiModel(rank, "Reader $rank", "AI", "$rank 권")
            }
            val placeholder = BookOnRankingMemberUiModel(0, "", "", "")
            BookOnTheme {
                BookOnRankingScreen(
                    uiState = BookOnRankingScreenUiState(
                        description = "2026",
                        isEmpty = members.isEmpty(),
                        podium = BookOnRankingPodiumUiModel(
                            members.getOrElse(0) { placeholder },
                            members.getOrElse(1) { placeholder },
                            members.getOrElse(2) { placeholder },
                        ),
                        list = BookOnRankingListUiModel(members.drop(3)),
                    ),
                    bottomBar = {},
                    onRetryClick = {},
                )
            }
        }
        composeTestRule.onNodeWithText("표시할 랭킹이 없어요.").assertExists()
        for (count in 1..4) {
            composeTestRule.runOnIdle { readerCount.intValue = count }
            composeTestRule.onNodeWithText("표시할 랭킹이 없어요.").assertDoesNotExist()
        }
    }
}
