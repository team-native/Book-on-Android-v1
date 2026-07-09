package com.example.bookon.ui.commonComponent.marathon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookon.R
import com.example.bookon.theme.AppIconSize
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.marathon.BookOnMarathonLinkCardUiState

private val MarathonLinkCardHeight = 92.dp

private const val MarathonLinkCardPreviewTitle = "독서마라톤 이용하기"
private const val MarathonLinkCardPreviewDescription = "교내 독서마라톤에 참여 중이라면 연동하세요"

/**
 * 독서마라톤 이용 안내나 연동 진입을 표시하는 기능 전용 카드이다.
 * onClick이 null이면 읽기 전용 안내 카드로 표시한다.
 */
@Composable
fun BookOnMarathonLinkCard(
    uiState: BookOnMarathonLinkCardUiState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    BookOnMarathonLinkCard(
        title = uiState.title,
        description = uiState.description,
        modifier = modifier,
        iconRes = uiState.iconRes,
        iconContentDescription = uiState.iconContentDescription,
        onClick = onClick,
    )
}

/**
 * 독서마라톤 이용 안내나 연동 진입을 표시하는 기능 전용 카드이다.
 * onClick이 null이면 읽기 전용 안내 카드로 표시한다.
 */
@Composable
fun BookOnMarathonLinkCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int = R.drawable.marathon_logo,
    iconContentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MarathonLinkCardHeight)
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(BookOnColor.Surface)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(AppIconSize.XLarge),
            painter = painterResource(iconRes),
            contentDescription = iconContentDescription,
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Content))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Tiny))
            Text(
                text = description,
                style = BookOnTypography.caption,
                color = BookOnColor.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMarathonLinkCardPreview() {
    BookOnTheme {
        BookOnMarathonLinkCard(
            title = MarathonLinkCardPreviewTitle,
            description = MarathonLinkCardPreviewDescription,
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
