package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar

/** 인증 화면의 뒤로가기와 비밀번호 유의사항 툴팁을 제공하는 상단바이다. */
@Composable
internal fun BookOnAuthTopBar(
    onBackClick: () -> Unit,
) {
    BookOnTopBar(
        title = "",
        onBackClick = onBackClick,
        trailingContent = {
            BookOnPasswordPolicyTooltip()
        },
    )
}
