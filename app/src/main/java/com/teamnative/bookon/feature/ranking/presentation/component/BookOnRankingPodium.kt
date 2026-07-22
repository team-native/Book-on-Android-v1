package com.teamnative.bookon.feature.ranking.presentation.component

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingMemberUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingPodiumUiModel

/**
 * 랭킹 화면의 상위 1~3위 사용자와 포디움 이미지를 표시한다.
 * 각 사용자 정보는 이미 화면용 문자열로 가공된 상태로 전달받는다.
 */
@Composable
fun BookOnRankingPodium(
    uiState: BookOnRankingPodiumUiModel,
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
    first: BookOnRankingMemberUiModel,
    second: BookOnRankingMemberUiModel,
    third: BookOnRankingMemberUiModel,
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
            avatarSize = AppIconSize.PodiumSecondaryAvatar,
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
            avatarSize = AppIconSize.PodiumSecondaryAvatar,
            bookCountColor = BookOnColor.TextTertiary,
        )
    }
}

@Composable
private fun RankingPodiumPlace(
    member: BookOnRankingMemberUiModel,
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
            first = BookOnRankingMemberUiModel(1, "홍길동", "3학년 · AI과", "49 권"),
            second = BookOnRankingMemberUiModel(2, "김길동", "2학년 · 소프트웨어 개발과", "46 권"),
            third = BookOnRankingMemberUiModel(3, "이길동", "1학년 · AI과", "45 권"),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
