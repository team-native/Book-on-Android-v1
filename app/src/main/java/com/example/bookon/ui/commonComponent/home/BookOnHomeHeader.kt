package com.example.bookon.ui.commonComponent.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.bookon.R
import com.example.bookon.theme.AppComponentSize
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTypography

/**
 * 홈 상단의 인사말, 사용자 이름, 알림과 프로필 액션을 피그마 메인 화면 구조로 표시한다.
 * 알림 또는 프로필 callback이 null이면 해당 액션은 클릭 없이 정보성 아이콘으로만 렌더링한다.
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
    Box(
        modifier = modifier
            .size(AppComponentSize.HomeActionButton)
            .clip(RoundedCornerShape(AppRadius.IconButton))
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
        )
    }
}
