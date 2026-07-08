package com.example.bookon.ui.commonComponent.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookon.R
import com.example.bookon.ui.theme.AppComponentSize
import com.example.bookon.ui.theme.AppElevation
import com.example.bookon.ui.theme.AppIconSize
import com.example.bookon.ui.theme.AppRadius
import com.example.bookon.ui.theme.AppSpacing
import com.example.bookon.ui.theme.BookOnColor
import com.example.bookon.ui.theme.BookOnTheme
import com.example.bookon.ui.theme.BookOnTypography
import com.example.bookon.uiState.home.BookOnHomeNoticeUiState
import com.example.bookon.uiState.home.BookOnHomeSectionHeaderUiState
import com.example.bookon.uiState.home.BookOnPopularBookRowUiState

/**
 * 홈 화면 섹션 제목과 선택적 우측 액션을 한 줄로 표시한다.
 * actionText가 null이거나 onActionClick이 null이면 제목만 표시한다.
 */
@Composable
fun BookOnHomeSectionHeader(
    uiState: BookOnHomeSectionHeaderUiState,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = BookOnTypography.sectionTitle,
    onActionClick: (() -> Unit)? = null,
) {
    BookOnHomeSectionHeader(
        title = uiState.title,
        modifier = modifier,
        actionText = uiState.actionText,
        titleStyle = titleStyle,
        onActionClick = onActionClick,
    )
}

/**
 * 홈 화면 섹션 제목과 선택적 우측 액션을 한 줄로 표시한다.
 * actionText가 null이거나 onActionClick이 null이면 제목만 표시한다.
 */
@Composable
fun BookOnHomeSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    titleStyle: TextStyle = BookOnTypography.sectionTitle,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = titleStyle,
            color = BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
}

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

/**
 * 홈의 인기 책 영역처럼 작은 표지 placeholder와 책 정보를 가로 카드로 표시한다.
 * cover slot을 통해 실제 표지 로더를 연결할 수 있지만 기본값은 placeholder이다.
 */
@Composable
fun BookOnPopularBookRow(
    uiState: BookOnPopularBookRowUiState,
    modifier: Modifier = Modifier,
    cover: @Composable () -> Unit = { PopularBookCoverPlaceholder() },
) {
    BookOnPopularBookRow(
        title = uiState.title,
        metaText = uiState.metaText,
        modifier = modifier,
        statusText = uiState.statusText,
        cover = cover,
    )
}

/**
 * 홈의 인기 책 영역처럼 작은 표지 placeholder와 책 정보를 가로 카드로 표시한다.
 * cover slot을 통해 실제 표지 로더를 연결할 수 있지만 기본값은 placeholder이다.
 */
@Composable
fun BookOnPopularBookRow(
    title: String,
    metaText: String,
    modifier: Modifier = Modifier,
    statusText: String? = null,
    cover: @Composable () -> Unit = { PopularBookCoverPlaceholder() },
) {
    Row(
        modifier = modifier
            .width(206.dp)
            .height(76.dp)
            .shadow(
                elevation = AppElevation.Card,
                shape = RoundedCornerShape(AppRadius.Chip),
            )
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(BookOnColor.Background)
            .padding(AppSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(AppComponentSize.PopularBookCoverWidth)
                .height(AppComponentSize.PopularBookCoverHeight)
                .clip(RoundedCornerShape(AppRadius.IconButton)),
            contentAlignment = Alignment.Center,
        ) {
            cover()
        }
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = BookOnTypography.caption,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Tiny))
            Text(
                text = metaText,
                style = BookOnTypography.bookMeta,
                color = BookOnColor.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (statusText != null) {
                Text(
                    text = statusText,
                    style = BookOnTypography.bookMeta,
                    color = BookOnColor.PrimaryPressed,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun PopularBookCoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BookOnColor.BookCoverSmallPlaceholder),
    )
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

@Preview(showBackground = true)
@Composable
private fun BookOnPopularBookRowPreview() {
    BookOnTheme {
        BookOnPopularBookRow(
            title = "소년이 온다",
            metaText = "한강 · 재고 3권",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
