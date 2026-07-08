package com.example.bookon.ui.commonComponent.marathon

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
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookon.R
import com.example.bookon.ui.theme.AppIconSize
import com.example.bookon.ui.theme.AppRadius
import com.example.bookon.ui.theme.AppSpacing
import com.example.bookon.ui.theme.BookOnColor
import com.example.bookon.ui.theme.BookOnTheme
import com.example.bookon.ui.theme.BookOnTypography
import com.example.bookon.uiState.marathon.BookOnMarathonAgreementUiState
import com.example.bookon.uiState.marathon.BookOnMarathonCircleActionUiState
import com.example.bookon.uiState.marathon.BookOnMarathonLinkCardUiState

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
            .height(92.dp)
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

/**
 * 독서마라톤 계정 연동 동의처럼 체크 이미지와 문구를 함께 표시한다.
 * checked 상태는 toggleable semantics에 연결한다.
 */
@Composable
fun BookOnMarathonAgreementRow(
    uiState: BookOnMarathonAgreementUiState,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnMarathonAgreementRow(
        text = uiState.text,
        checked = uiState.checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = uiState.enabled,
    )
}

/**
 * 독서마라톤 계정 연동 동의처럼 체크 이미지와 문구를 함께 표시한다.
 * checked 상태는 toggleable semantics에 연결한다.
 */
@Composable
fun BookOnMarathonAgreementRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
            .alpha(if (enabled) 1f else 0.55f),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(
                if (checked) {
                    R.drawable.authority_check
                } else {
                    R.drawable.authority_not_check
                },
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = BookOnTypography.caption,
            color = BookOnColor.TextTertiary,
        )
    }
}

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
            .size(44.dp)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onSelected,
            )
            .alpha(if (enabled) 1f else 0.55f),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(44.dp),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMarathonLinkCardPreview() {
    BookOnTheme {
        BookOnMarathonLinkCard(
            title = "독서마라톤 이용하기",
            description = "교내 독서마라톤에 참여 중이라면 연동하세요",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMarathonAgreementRowPreview() {
    BookOnTheme {
        BookOnMarathonAgreementRow(
            text = "독서마라톤 계정 연동을 위한 개인정보 제3자 제공에 동의합니다.",
            checked = true,
            onCheckedChange = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
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
                contentDescription = "네이버",
                onSelected = {},
            )
            Spacer(modifier = Modifier.width(AppSpacing.Content))
            BookOnMarathonCircleAction(
                iconRes = R.drawable.oauth_google,
                contentDescription = "구글",
                onSelected = {},
            )
        }
    }
}
