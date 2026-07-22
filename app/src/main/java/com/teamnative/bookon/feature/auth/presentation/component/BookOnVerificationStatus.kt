package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

/**
 * 인증번호 입력 아래의 오류, 만료 시간, 재전송 액션 상태를 표시한다.
 * 재전송 클릭은 호출 화면의 인증 이벤트로 위임한다.
 */
@Composable
fun BookOnVerificationStatus(
    expireText: String,
    resendText: String,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
    ) {
        if (errorText != null) {
            Text(
                text = errorText,
                style = BookOnTypography.caption,
                color = BookOnColor.Error,
            )
        }
        Text(
            text = expireText,
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
        )
        Text(
            modifier = Modifier.clickable(role = Role.Button, onClick = onResendClick),
            text = resendText,
            style = BookOnTypography.caption,
            color = BookOnColor.PrimaryPressed,
        )
    }
}
