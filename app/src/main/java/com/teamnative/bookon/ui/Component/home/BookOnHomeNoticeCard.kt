package com.teamnative.bookon.ui.Component.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppComponentSize
import com.teamnative.bookon.theme.AppElevation
import com.teamnative.bookon.theme.AppIconSize
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.home.BookOnHomeNoticeUiState

/**
 * 홈 화면 도서부 공지처럼 아이콘, 날짜, 배지, 본문, 상세 액션을 포함한 카드이다.
 * onClick이 null이면 카드가 읽기 전용으로 동작한다.
 */
@Composable
fun BookOnHomeNoticeCard(
    uiState: BookOnHomeNoticeUiState,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onClick: (() -> Unit)? = null,
) {
    BookOnHomeNoticeCard(
        category = uiState.category,
        dateText = uiState.dateText,
        title = uiState.title,
        description = uiState.description,
        modifier = modifier,
        badgeText = uiState.badgeText,
        iconRes = uiState.iconRes,
        iconContentDescription = uiState.iconContentDescription,
        actionText = actionText,
        onClick = onClick,
    )
}

/**
 * 홈 화면 도서부 공지처럼 아이콘, 날짜, 배지, 본문, 상세 액션을 포함한 카드이다.
 * onClick이 null이면 카드가 읽기 전용으로 동작한다.
 */
@Composable
fun BookOnHomeNoticeCard(
    category: String,
    dateText: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    @DrawableRes iconRes: Int = R.drawable.home_notification,
    iconContentDescription: String? = null,
    actionText: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppElevation.Card,
                shape = RoundedCornerShape(AppRadius.LargeCard),
            )
            .clip(RoundedCornerShape(AppRadius.LargeCard))
            .background(BookOnColor.Surface)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(AppSpacing.Content),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(AppIconSize.XLarge),
                painter = painterResource(iconRes),
                contentDescription = iconContentDescription,
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.width(AppSpacing.Item))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    style = BookOnTypography.bodySemiBold,
                    color = BookOnColor.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = dateText,
                    style = BookOnTypography.bookMeta,
                    color = BookOnColor.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .height(AppComponentSize.SmallChipHeight)
                        .clip(RoundedCornerShape(AppRadius.Small))
                        .background(BookOnColor.Primary)
                        .padding(horizontal = AppSpacing.Item),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = badgeText,
                        style = BookOnTypography.badge,
                        color = BookOnColor.Surface,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.Content))
        Text(
            text = title,
            style = BookOnTypography.bodySemiBold,
            color = BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        Text(
            text = description,
            style = BookOnTypography.bodyMedium,
            color = BookOnColor.TextSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        if (actionText != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Item))
            Text(
                text = actionText,
                style = BookOnTypography.caption,
                color = BookOnColor.PrimaryPressed,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnHomeNoticeCardPreview() {
    BookOnTheme {
        BookOnHomeNoticeCard(
            category = "도서부 공지",
            dateText = "2026. 07. 01 · 도서부",
            title = "여름방학 도서 대출 기간 연장 안내",
            description = "방학 기간 동안 1인당 최대 5권, 대출 기간이 14일로 연장됩니다.",
            badgeText = "NEW",
            actionText = "자세히 보기",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
