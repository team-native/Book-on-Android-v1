package com.example.bookon.ui.commonComponent.ranking

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.bookon.uiState.ranking.BookOnRankingListUiState
import com.example.bookon.uiState.ranking.BookOnRankingMemberUiState
import com.example.bookon.uiState.ranking.BookOnRankingPodiumUiState

/**
 * 랭킹 화면의 상위 1~3위 사용자와 포디움 이미지를 표시한다.
 * 각 사용자 정보는 이미 화면용 문자열로 가공된 상태로 전달받는다.
 */
@Composable
fun BookOnRankingPodium(
    uiState: BookOnRankingPodiumUiState,
    modifier: Modifier = Modifier,
) {
    BookOnRankingPodium(
        first = uiState.first,
        second = uiState.second,
        third = uiState.third,
        modifier = modifier,
    )
}

/**
 * 랭킹 화면의 상위 1~3위 사용자와 포디움 이미지를 표시한다.
 * 각 사용자 정보는 이미 화면용 문자열로 가공된 상태로 전달받는다.
 */
@Composable
fun BookOnRankingPodium(
    first: BookOnRankingMemberUiState,
    second: BookOnRankingMemberUiState,
    third: BookOnRankingMemberUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        RankingPodiumPlace(
            member = second,
            pedestalRes = R.drawable.rank_2,
            pedestalHeight = AppComponentSize.RankingSecondPedestalHeight,
            avatarSize = 52.dp,
            bookCountColor = BookOnColor.TextTertiary,
        )
        RankingPodiumPlace(
            member = first,
            pedestalRes = R.drawable.rank_1,
            pedestalHeight = AppComponentSize.RankingFirstPedestalHeight,
            avatarSize = AppIconSize.Avatar,
            bookCountColor = BookOnColor.PrimaryPressed,
        )
        RankingPodiumPlace(
            member = third,
            pedestalRes = R.drawable.rank_3,
            pedestalHeight = AppComponentSize.RankingThirdPedestalHeight,
            avatarSize = 52.dp,
            bookCountColor = BookOnColor.TextTertiary,
        )
    }
}

/**
 * 4위 이후 랭킹 사용자를 카드 목록으로 표시한다.
 * 목록이 비어 있으면 카드 영역을 렌더링하지 않는다.
 */
@Composable
fun BookOnRankingListCard(
    uiState: BookOnRankingListUiState,
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
    members: List<BookOnRankingMemberUiState>,
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
private fun RankingPodiumPlace(
    member: BookOnRankingMemberUiState,
    @DrawableRes pedestalRes: Int,
    pedestalHeight: androidx.compose.ui.unit.Dp,
    avatarSize: androidx.compose.ui.unit.Dp,
    bookCountColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(AppComponentSize.RankingPedestalWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RankingAvatar(
            avatarRes = member.avatarRes,
            contentDescription = member.avatarContentDescription,
            modifier = Modifier.size(avatarSize),
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        Text(
            text = member.name,
            style = BookOnTypography.bodySemiBold,
            color = BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = member.bookCountText,
            style = BookOnTypography.caption,
            color = bookCountColor,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Item))
        Image(
            modifier = Modifier
                .width(AppComponentSize.RankingPedestalWidth)
                .height(pedestalHeight),
            painter = painterResource(pedestalRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
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

@Preview(showBackground = true)
@Composable
private fun BookOnRankingPodiumPreview() {
    BookOnTheme {
        BookOnRankingPodium(
            first = BookOnRankingMemberUiState(1, "홍길동", "3학년 · AI과", "49 권"),
            second = BookOnRankingMemberUiState(2, "김길동", "2학년 · 소프트웨어 개발과", "46 권"),
            third = BookOnRankingMemberUiState(3, "이길동", "1학년 · AI과", "45 권"),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnRankingListCardPreview() {
    BookOnTheme {
        BookOnRankingListCard(
            members = listOf(
                BookOnRankingMemberUiState(4, "정길동", "2학년 · 소프트웨어 개발과", "31권"),
                BookOnRankingMemberUiState(5, "최길동", "1학년 · AI과", "28권"),
                BookOnRankingMemberUiState(6, "한길동", "2학년 · 소프트웨어 개발과", "20권"),
            ),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
