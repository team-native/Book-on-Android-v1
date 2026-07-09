package com.example.bookon.ui.commonComponent.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.theme.AppIconSize
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography

/**
 * 내 서재 상단의 사용자 이름, 학생 정보, 프로필 placeholder를 표시한다.
 * 실제 프로필 이미지는 서버 이미지 로더가 준비되면 avatar slot으로 교체한다.
 */
@Composable
fun BookOnMyProfileHeader(
    userNameText: String,
    studentInfoText: String,
    modifier: Modifier = Modifier,
    avatar: @Composable () -> Unit = { MyAvatarPlaceholder() },
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userNameText,
                style = BookOnTypography.sectionTitle,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Tiny))
            Text(
                text = studentInfoText,
                style = BookOnTypography.caption,
                color = BookOnColor.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            modifier = Modifier
                .size(AppIconSize.Avatar)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            avatar()
        }
    }
}

@Composable
private fun MyAvatarPlaceholder() {
    Box(
        modifier = Modifier
            .size(AppIconSize.Avatar)
            .clip(CircleShape)
            .background(BookOnColor.IconContainer),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnMyProfileHeaderPreview() {
    BookOnTheme {
        BookOnMyProfileHeader(
            userNameText = "홍길동 님",
            studentInfoText = "10기 · 소프트웨어 개발과",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
