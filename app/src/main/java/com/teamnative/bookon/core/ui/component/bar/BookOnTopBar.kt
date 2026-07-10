package com.teamnative.bookon.core.ui.component.bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
/**
 * 화면 상단 제목과 뒤로가기, 우측 액션 영역을 제공한다.
 * Android 시스템 status bar 영역은 앱 Scaffold나 WindowInsets 정책에 맡긴다.
 */
@Composable
fun BookOnTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    backContentDescription: String = "뒤로가기",
    trailingContent: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.TopBarHeight),
    ) {
        if (onBackClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(RoundedCornerShape(AppRadius.IconButton))
                    .background(BookOnColor.Background)
                    .clickable(
                        role = Role.Button,
                        onClickLabel = backContentDescription,
                        onClick = onBackClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                BackChevron()
            }
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = BookOnTypography.topBarTitle,
            color = BookOnColor.TextPrimary,
        )

        Box(
            modifier = Modifier.align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center,
        ) {
            trailingContent()
        }
    }
}

@Composable
private fun BackChevron() {
    Canvas(modifier = Modifier.size(AppIconSize.Small)) {
        val strokeWidth = 2.dp.toPx()
        val startX = size.width * 0.6f
        val centerX = size.width * 0.35f
        drawLine(
            color = BookOnColor.TextPrimary,
            start = Offset(startX, size.height * 0.2f),
            end = Offset(centerX, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = BookOnColor.TextPrimary,
            start = Offset(centerX, size.height * 0.5f),
            end = Offset(startX, size.height * 0.8f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnTopBarPreview() {
    BookOnTheme {
        BookOnTopBar(
            title = "내 서재",
            onBackClick = {},
        )
    }
}
