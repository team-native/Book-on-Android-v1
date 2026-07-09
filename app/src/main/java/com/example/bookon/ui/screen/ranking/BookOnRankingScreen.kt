package com.example.bookon.ui.screen.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.R
import com.example.bookon.ui.commonComponent.ranking.BookOnRankingListCard
import com.example.bookon.ui.commonComponent.ranking.BookOnRankingPodium
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.ranking.BookOnRankingListUiState
import com.example.bookon.uiState.ranking.BookOnRankingMemberUiState
import com.example.bookon.uiState.ranking.BookOnRankingPodiumUiState
import com.example.bookon.uiState.ranking.BookOnRankingScreenUiState

/**
 * 랭킹 Route는 서버 랭킹 연동 전 샘플 랭킹 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnRankingRoute(bottomBar: @Composable () -> Unit) {
    BookOnRankingScreen(
        uiState = previewRankingUiState(),
        bottomBar = bottomBar,
    )
}

/**
 * 랭킹 화면은 포디움과 4위 이후 목록을 기존 랭킹 컴포넌트로 조립한다.
 */
@Composable
fun BookOnRankingScreen(
    uiState: BookOnRankingScreenUiState,
    bottomBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = BookOnColor.Background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Tiny)) {
                    Text(
                        text = stringResource(R.string.ranking_title),
                        style = BookOnTypography.screenTitle,
                        color = BookOnColor.TextPrimary,
                    )
                    Text(
                        text = uiState.description,
                        style = BookOnTypography.caption,
                        color = BookOnColor.TextSecondary,
                    )
                }
            }
            item {
                BookOnRankingPodium(uiState = uiState.podium)
            }
            item {
                BookOnRankingListCard(uiState = uiState.list)
            }
        }
    }
}

private fun previewRankingUiState() = BookOnRankingScreenUiState(
    description = "2026년 · 대출 권수 기준 · 매년 1월 1일 초기화",
    podium = BookOnRankingPodiumUiState(
        first = BookOnRankingMemberUiState(1, "홍길동", "3학년 · AI과", "49 권"),
        second = BookOnRankingMemberUiState(2, "김길동", "2학년 · 소프트웨어 개발과", "46 권"),
        third = BookOnRankingMemberUiState(3, "이길동", "1학년 · AI과", "45 권"),
    ),
    list = BookOnRankingListUiState(
        members = listOf(
            BookOnRankingMemberUiState(4, "정길동", "2학년 · 소프트웨어 개발과", "31권"),
            BookOnRankingMemberUiState(5, "최길동", "1학년 · AI과", "28권"),
            BookOnRankingMemberUiState(6, "한길동", "2학년 · 소프트웨어 개발과", "20권"),
        ),
    ),
)

@Preview(showBackground = true)
@Composable
private fun BookOnRankingScreenPreview() {
    BookOnTheme {
        BookOnRankingScreen(
            uiState = previewRankingUiState(),
            bottomBar = {},
        )
    }
}
