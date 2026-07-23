package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthTopBar
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader
import com.teamnative.bookon.feature.auth.presentation.component.BookOnVerificationCodeField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnVerificationStatus

/** 비밀번호 재설정 두 번째 단계에서 이메일로 받은 인증번호를 입력받는다. */
@Composable
fun BookOnPasswordResetVerificationScreen(
    uiState: BookOnPasswordResetUiState,
    onEvent: (BookOnPasswordResetScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var progressAnimating by remember { mutableStateOf(true) }

    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = {
            BookOnAuthTopBar(
                onBackClick = { onEvent(BookOnPasswordResetScreenEvent.BackClicked) },
            )
        },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_verify_continue),
                onClick = { onEvent(BookOnPasswordResetScreenEvent.ContinueClicked) },
                enabled = uiState.nextEnabled && !uiState.isLoading && !progressAnimating,
            )
        },
    ) {
        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))

        BookOnSignupStepHeader(
            step = PasswordResetVerificationStep,
            title = uiState.title,
            description = uiState.description,
            onProgressAnimationRunningChange = { progressAnimating = it },
        )

        BookOnVerificationCodeField(
            code = uiState.verificationCode,
            onCodeChange = { code ->
                onEvent(BookOnPasswordResetScreenEvent.VerificationCodeChanged(code))
            },
            isError = uiState.errorText != null,
        )
        BookOnVerificationStatus(
            expireText = stringResource(R.string.password_reset_verification_expire),
            resendText = stringResource(R.string.verification_code_resend),
            onResendClick = { onEvent(BookOnPasswordResetScreenEvent.ResendClicked) },
            errorText = uiState.errorText,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordResetVerificationScreenPreview() {
    BookOnTheme {
        BookOnPasswordResetVerificationScreen(
            uiState = defaultPasswordResetUiState().copy(
                title = stringResource(R.string.password_reset_verification_title),
                description = stringResource(
                    R.string.password_reset_verification_description,
                    "s26031",
                ),
            ),
            onEvent = {},
        )
    }
}
