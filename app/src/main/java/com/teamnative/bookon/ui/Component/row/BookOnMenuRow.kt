package com.teamnative.bookon.ui.Component.row

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppIconSize
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography

/**
 * 마이페이지 설정 목록처럼 제목, 우측 액션, 하단 구분선을 가진 행이다.
 * onClick이 null이면 읽기 전용 행으로 표시한다.
 */
@Composable
fun BookOnMenuRow(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    destructive: Boolean = false,
    showDivider: Boolean = true,
    trailingContent: @Composable () -> Unit = { MenuChevron() },
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(role = Role.Button, onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = BookOnTypography.bodySemiBold,
                color = if (destructive) BookOnColor.Error else BookOnColor.TextPrimary,
            )
            trailingContent()
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BookOnColor.Divider),
            )
        }
    }
}

@Composable
private fun MenuChevron() {
    Canvas(modifier = Modifier.size(AppIconSize.Small)) {
        val strokeWidth = 1.5.dp.toPx()
        val startX = size.width * 0.35f
        val endX = size.width * 0.65f
        drawLine(
            color = BookOnColor.TextPlaceholder,
            start = Offset(startX, size.height * 0.2f),
            end = Offset(endX, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = BookOnColor.TextPlaceholder,
            start = Offset(endX, size.height * 0.5f),
            end = Offset(startX, size.height * 0.8f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMenuRowPreview() {
    BookOnTheme {
        BookOnMenuRow(
            title = "대출 / 반납 내역",
            onClick = {},
        )
    }
}
