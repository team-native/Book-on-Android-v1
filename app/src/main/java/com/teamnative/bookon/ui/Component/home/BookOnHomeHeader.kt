package com.teamnative.bookon.ui.Component.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppComponentSize
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography

/**
 * 홈 상단의 인사말, 사용자 이름, 알림과 프로필 액션을 피그마 메인 화면 구조로 표시한다.
 * 각 액션은 서버 통신 또는 화면 이동 이벤트를 호출부에서 연결할 수 있도록 버튼 callback으로 노출한다.
 */
@Composable
fun BookOnHomeHeader(
    greeting: String,
    userName: String,
    notificationContentDescription: String,
    profileContentDescription: String,
    modifier: Modifier = Modifier,
    onNotificationClick: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                style = BookOnTypography.caption,
                color = BookOnColor.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Tiny))

            Text(
                text = userName,
                style = BookOnTypography.screenTitle,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        BookOnHomeIconButton(
            iconRes = R.drawable.notification,
            contentDescription = notificationContentDescription,
            onClick = onNotificationClick,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Content))

        BookOnHomeIconButton(
            iconRes = R.drawable.main_profile,
            contentDescription = profileContentDescription,
            onClick = onProfileClick,
        )
    }
}

@Composable
private fun BookOnHomeIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    IconButton(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = modifier
            .size(AppComponentSize.HomeActionButton)
            .clip(RoundedCornerShape(AppRadius.IconButton)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = BookOnColor.TextPrimary,
            disabledContentColor = BookOnColor.TextPrimary,
        ),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
        )
    }
}
