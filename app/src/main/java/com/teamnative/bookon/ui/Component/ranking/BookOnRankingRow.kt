package com.teamnative.bookon.ui.Component.ranking

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.teamnative.bookon.theme.AppComponentSize
import com.teamnative.bookon.theme.AppIconSize
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.ranking.BookOnRankingMemberUiState

/**
 * 랭킹 카드 내부의 한 사용자 행이다.
 * rank, 사용자 정보, 권수 표시가 한 줄에서 안정적으로 줄임 처리되도록 구성한다.
 */
@Composable
fun BookOnRankingRow(
    member: BookOnRankingMemberUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(AppComponentSize.RankingListRowHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(AppIconSize.Default),
            text = member.rank.toString(),
            style = BookOnTypography.bodySemiBold,
            color = BookOnColor.TextSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Item))
        RankingAvatar(
            avatarRes = member.avatarRes,
            contentDescription = member.avatarContentDescription,
            modifier = Modifier.size(AppIconSize.Medium),
        )
        Spacer(modifier = Modifier.width(AppSpacing.Item))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = member.name,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = member.description,
                style = BookOnTypography.caption,
                color = BookOnColor.TextTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.width(AppSpacing.Item))
        Text(
            text = member.bookCountText,
            style = BookOnTypography.caption,
            color = BookOnColor.TextPrimary,
            maxLines = 1,
        )
    }
}

@Composable
private fun RankingAvatar(
    @DrawableRes avatarRes: Int?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(BookOnColor.IconContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (avatarRes != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(avatarRes),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier
                    .size(AppIconSize.Default)
                    .clip(CircleShape)
                    .background(BookOnColor.Surface),
            )
        }
    }
}
