package com.teamnative.bookon.ui.Component.button

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.draw.clip
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography

/**
 * 마이페이지에서 토큰 반납과 세션 정리로 이어질 로그아웃 요청을 전달하는 텍스트 버튼이다.
 * 실제 인증 상태 변경은 상위 Route 또는 ViewModel의 onLogoutRequest에서 처리한다.
 */
@Composable
fun BookOnLogoutButton(
    onLogoutRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .clip(MaterialTheme.shapes.large),
        onClick = onLogoutRequest,
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = stringResource(R.string.action_logout),
            style = BookOnTypography.bodySemiBold,
            color = BookOnColor.Error,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnLogoutButtonPreview() {
    BookOnTheme {
        BookOnLogoutButton(onLogoutRequest = {})
    }
}
