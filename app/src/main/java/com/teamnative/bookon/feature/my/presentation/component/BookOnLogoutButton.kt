package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

/**
 * 마이페이지에서 토큰 반납과 세션 정리로 이어질 로그아웃 요청을 전달하는 텍스트 버튼이다.
 * 실제 인증 상태 변경은 상위 Route 또는 ViewModel의 onLogoutRequest에서 처리한다.
 */
@Composable
fun BookOnLogoutButton(
    onLogoutRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(AppRadius.Small))
            .clickable(role = Role.Button, onClick = onLogoutRequest),
        text = stringResource(R.string.action_logout),
        style = bookOnTypography.bodySemiBold,
        color = MaterialTheme.colorScheme.error,
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnLogoutButtonPreview() {
    BookOnTheme {
        BookOnLogoutButton(onLogoutRequest = {})
    }
}
