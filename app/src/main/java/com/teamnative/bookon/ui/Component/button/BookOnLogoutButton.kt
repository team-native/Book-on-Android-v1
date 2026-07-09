package com.teamnative.bookon.ui.Component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.ui.Component.row.BookOnMenuRow

/**
 * 마이페이지 메뉴 목록과 분리된 로그아웃 전용 버튼이다.
 */
@Composable
fun BookOnLogoutButton(onClick: () -> Unit) {
    BookOnMenuRow(
        title = stringResource(R.string.action_logout),
        onClick = onClick,
        destructive = true,
        showDivider = false,
    )
}
