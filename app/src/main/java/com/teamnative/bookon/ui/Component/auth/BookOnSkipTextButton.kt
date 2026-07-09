package com.teamnative.bookon.ui.Component.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography

/**
 * 독서마라톤 연동을 나중으로 미루는 하단 텍스트 액션이다.
 * 클릭 처리는 회원가입 흐름의 navigation callback으로 위임한다.
 */
@Composable
fun BookOnSkipTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        text = text,
        style = BookOnTypography.caption,
        color = BookOnColor.TextSecondary,
        textAlign = TextAlign.Center,
    )
}
