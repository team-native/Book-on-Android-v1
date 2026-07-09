package com.teamnative.bookon.ui.Component.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography

/**
 * AI 추천 섹션의 제목, AI 배지, 설명과 우측 더보기 액션을 한 블록으로 표시한다.
 * 피그마 메인 화면처럼 설명은 제목 아래 보조 텍스트로 배치한다.
 */
@Composable
fun BookOnHomeAiSectionHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = BookOnTypography.sectionTitle,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (badgeText != null) {
                Spacer(modifier = Modifier.width(AppSpacing.Small))
                Row(
                    modifier = Modifier
                        .height(22.dp)
                        .clip(RoundedCornerShape(AppRadius.Small))
                        .background(BookOnColor.Primary)
                        .padding(horizontal = AppSpacing.Small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "+ $badgeText",
                        style = BookOnTypography.badge,
                        color = BookOnColor.Surface,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (actionText != null && onActionClick != null) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(AppRadius.Small))
                        .clickable(role = Role.Button, onClick = onActionClick)
                        .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Tiny),
                    text = actionText,
                    style = BookOnTypography.caption,
                    color = BookOnColor.PrimaryPressed,
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.Tiny))
        Text(
            text = description,
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
