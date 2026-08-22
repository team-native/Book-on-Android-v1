package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader
import com.teamnative.bookon.feature.auth.presentation.component.BookOnVerificationCodeField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnVerificationStatus
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme

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
    initialProgressStep: Int? = null,
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

        BookOnSignupStepHeader(
            step = 2,
            title = uiState.title,
            description = uiState.description,
            initialProgressStep = initialProgressStep,
        )
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

@Preview(showBackground = true)
@Composable
private fun BookOnVerificationCodeScreenPreview() {
    BookOnTheme {
        BookOnVerificationCodeScreen(
            uiState = defaultVerificationCodeUiState(),
            onBackClick = {}, onCodeChange = {}, onResendClick = {}, onConfirmClick = {},
        )
    }
}
