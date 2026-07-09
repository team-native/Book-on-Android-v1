package com.teamnative.bookon.ui.Component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.ui.Component.card.BookOnInfoCard
import com.teamnative.bookon.theme.AppSpacing

/**
 * 가입 완료 화면에서 보유 도서와 독서마라톤 연동 요약을 두 카드로 표시한다.
 * 표시 문자열은 호출 화면에서 resource 또는 UiState로 구성해 전달한다.
 */
@Composable
fun BookOnSignupCompleteSummary(
    ownedBookCountText: String,
    ownedBookDescription: String,
    marathonStatusText: String,
    marathonDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content),
    ) {
        BookOnInfoCard(
            title = ownedBookCountText,
            description = ownedBookDescription,
            modifier = Modifier.weight(1f),
        )
        BookOnInfoCard(
            title = marathonStatusText,
            description = marathonDescription,
            modifier = Modifier.weight(1f),
        )
    }
}
