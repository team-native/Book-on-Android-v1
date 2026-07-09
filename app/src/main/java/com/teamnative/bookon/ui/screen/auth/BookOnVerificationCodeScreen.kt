package com.teamnative.bookon.ui.screen.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.ui.Component.auth.BookOnAuthFormScaffold
import com.teamnative.bookon.ui.Component.auth.BookOnSignupStepHeader
import com.teamnative.bookon.ui.Component.auth.BookOnVerificationCodeField
import com.teamnative.bookon.ui.Component.auth.BookOnVerificationStatus
import com.teamnative.bookon.ui.Component.bar.BookOnTopBar
import com.teamnative.bookon.ui.Component.button.BookOnPrimaryButton
import com.teamnative.bookon.uiState.auth.BookOnVerificationCodeUiState

/**
 * 인증번호 화면은 6자리 코드 입력과 만료/재전송 상태를 표시한다.
 */
@Composable
fun BookOnVerificationCodeScreen(
    uiState: BookOnVerificationCodeUiState,
    onBackClick: () -> Unit,
    onCodeChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_verify_continue),
                onClick = onConfirmClick,
                enabled = uiState.confirmEnabled,
            )
        },
    ) {
        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))
        BookOnSignupStepHeader(step = 1, title = uiState.title, description = uiState.description)
        BookOnVerificationCodeField(
            code = uiState.code,
            onCodeChange = onCodeChange,
            isError = uiState.errorText != null,
        )
        BookOnVerificationStatus(
            expireText = uiState.expireText,
            resendText = stringResource(R.string.verification_code_resend),
            onResendClick = onResendClick,
            errorText = uiState.errorText,
        )
    }
}
