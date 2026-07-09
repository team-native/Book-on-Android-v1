package com.teamnative.bookon.ui.Component.marathon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.uiState.marathon.BookOnMarathonCircleActionUiState

private val MarathonCircleActionSize = 44.dp

private const val MarathonComponentEnabledAlpha = 1f
private const val MarathonComponentDisabledAlpha = 0.55f

private const val MarathonNaverContentDescription = "네이버"
private const val MarathonGoogleContentDescription = "구글"

/**
 * 독서마라톤 연동 화면의 원형 아이콘 선택 버튼이다.
 * Figma의 44dp OAuth 원형 아이콘을 그대로 표시하고 실제 기능은 onSelected로 위임한다.
 */
@Composable
fun BookOnMarathonCircleAction(
    uiState: BookOnMarathonCircleActionUiState,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnMarathonCircleAction(
        iconRes = uiState.iconRes,
        contentDescription = uiState.contentDescription,
        onSelected = onSelected,
        modifier = modifier,
        enabled = uiState.enabled,
    )
}

/**
 * 독서마라톤 연동 화면의 원형 아이콘 선택 버튼이다.
 * Figma의 44dp OAuth 원형 아이콘을 그대로 표시하고 실제 기능은 onSelected로 위임한다.
 */
@Composable
fun BookOnMarathonCircleAction(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(MarathonCircleActionSize)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onSelected,
            )
            .alpha(if (enabled) MarathonComponentEnabledAlpha else MarathonComponentDisabledAlpha),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(MarathonCircleActionSize),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMarathonCircleActionPreview() {
    BookOnTheme {
        Row(modifier = Modifier.padding(AppSpacing.ScreenHorizontal)) {
            BookOnMarathonCircleAction(
                iconRes = R.drawable.oauth_naver,
                contentDescription = MarathonNaverContentDescription,
                onSelected = {},
            )
            Spacer(modifier = Modifier.width(AppSpacing.Content))
            BookOnMarathonCircleAction(
                iconRes = R.drawable.oauth_google,
                contentDescription = MarathonGoogleContentDescription,
                onSelected = {},
            )
        }
    }
}
