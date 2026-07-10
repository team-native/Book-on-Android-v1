package com.teamnative.bookon.feature.ranking.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingListUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingMemberUiModel

/**
 * 4위 이후 랭킹 사용자를 카드 목록으로 표시한다.
 * 목록이 비어 있으면 카드 영역을 렌더링하지 않는다.
 */
@Composable
fun BookOnRankingListCard(
    uiState: BookOnRankingListUiModel,
    modifier: Modifier = Modifier,
) {
    BookOnRankingListCard(
        members = uiState.members,
        modifier = modifier,
    )
}

/**
 * 4위 이후 랭킹 사용자를 카드 목록으로 표시한다.
 * 목록이 비어 있으면 카드 영역을 렌더링하지 않는다.
 */
@Composable
fun BookOnRankingListCard(
    members: List<BookOnRankingMemberUiModel>,
    modifier: Modifier = Modifier,
) {
    if (members.isEmpty()) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppElevation.Card,
                shape = RoundedCornerShape(AppRadius.Card),
            )
            .clip(RoundedCornerShape(AppRadius.Card))
            .background(BookOnColor.Surface)
            .padding(horizontal = AppSpacing.Content),
    ) {
        members.forEachIndexed { index, member ->
            BookOnRankingRow(
                member = member,
                modifier = Modifier.fillMaxWidth(),
            )
            if (index < members.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(BookOnColor.Divider),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnRankingListCardPreview() {
    BookOnTheme {
        BookOnRankingListCard(
            members = listOf(
                BookOnRankingMemberUiModel(4, "정길동", "2학년 · 소프트웨어 개발과", "31권"),
                BookOnRankingMemberUiModel(5, "최길동", "1학년 · AI과", "28권"),
                BookOnRankingMemberUiModel(6, "한길동", "2학년 · 소프트웨어 개발과", "20권"),
            ),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
