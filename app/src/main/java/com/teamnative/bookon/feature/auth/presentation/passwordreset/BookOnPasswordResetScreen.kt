package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthPasswordField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthTopBar
import com.teamnative.bookon.feature.auth.presentation.component.BookOnEmailField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnVerificationCodeField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnVerificationStatus

/** 비밀번호 재설정의 이메일, 인증번호, 새 비밀번호 입력 화면을 상태 기반으로 그린다. */
@Composable
fun BookOnPasswordResetScreen(
    uiState: BookOnPasswordResetUiState,
    onEvent: (BookOnPasswordResetScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = {
            BookOnAuthTopBar(
                onBackClick = { onEvent(BookOnPasswordResetScreenEvent.BackClicked) },
            )
        },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(
                    if (uiState.step == BookOnPasswordResetStep.Password) {
                        R.string.action_reset_password
                    } else {
                        R.string.action_next
                    },
                ),
                onClick = { onEvent(BookOnPasswordResetScreenEvent.ContinueClicked) },
                enabled = uiState.nextEnabled,
            )
        },
    ) {
        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Content)) {
            BookOnPasswordResetProgress(currentStep = uiState.step)
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                Text(
                    text = uiState.title,
                    style = BookOnTypography.screenTitle,
                    color = BookOnColor.TextPrimary,
                )
                Text(
                    text = uiState.description,
                    style = BookOnTypography.bodyMedium,
                    color = BookOnColor.TextSecondary,
                )
            }
        }

        when (uiState.step) {
            BookOnPasswordResetStep.Email -> BookOnEmailField(
                uiState = uiState.email,
                onValueChange = { onEvent(BookOnPasswordResetScreenEvent.EmailChanged(it)) },
            )

            BookOnPasswordResetStep.Verification -> {
                BookOnVerificationCodeField(
                    code = uiState.verificationCode,
                    onCodeChange = { onEvent(BookOnPasswordResetScreenEvent.VerificationCodeChanged(it)) },
                )
                BookOnVerificationStatus(
                    expireText = stringResource(R.string.password_reset_verification_expire),
                    resendText = stringResource(R.string.verification_code_resend),
                    onResendClick = { onEvent(BookOnPasswordResetScreenEvent.ResendClicked) },
                )
            }

            BookOnPasswordResetStep.Password -> {
                BookOnAuthPasswordField(
                    uiState = uiState.password,
                    onValueChange = { onEvent(BookOnPasswordResetScreenEvent.PasswordChanged(it)) },
                )
                BookOnAuthPasswordField(
                    uiState = uiState.passwordConfirm,
                    onValueChange = { onEvent(BookOnPasswordResetScreenEvent.PasswordConfirmChanged(it)) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordResetEmailPreview() {
    BookOnTheme {
        BookOnPasswordResetScreen(uiState = samplePasswordResetUiState(), onEvent = {})
    }
}
