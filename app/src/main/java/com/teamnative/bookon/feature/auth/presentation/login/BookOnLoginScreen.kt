package com.teamnative.bookon.feature.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthPasswordField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnEmailField
import com.teamnative.bookon.feature.auth.presentation.component.LoginTitleTopSpacing

/**
 * 로그인 화면은 이메일, 비밀번호, 로그인 CTA, 회원가입 진입을 Figma 기준으로 표시한다.
 */
@Composable
fun BookOnLoginScreen(
    uiState: BookOnLoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.AuthHorizontal)
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(LoginTitleTopSpacing))
            Text(text = uiState.title, style = BookOnTypography.screenTitle, color = BookOnColor.TextPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            Text(
                text = stringResource(R.string.login_description),
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextTertiary,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section + AppSpacing.Content))
            BookOnEmailField(uiState = uiState.email, onValueChange = onEmailChange)
            Spacer(modifier = Modifier.height(AppSpacing.Content))
            BookOnAuthPasswordField(uiState = uiState.password, onValueChange = onPasswordChange)
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = AppSpacing.Item)
                    .clickable(role = Role.Button, onClick = onForgotPasswordClick),
                text = stringResource(R.string.forgot_password),
                style = BookOnTypography.caption,
                color = BookOnColor.Primary,
            )
            Spacer(modifier = Modifier.weight(1f))
            BookOnPrimaryButton(
                modifier = Modifier.padding(horizontal = AppSpacing.Small),
                text = stringResource(R.string.login),
                onClick = onLoginClick,
                enabled = uiState.loginEnabled,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(role = Role.Button, onClick = onSignupClick),
                text = stringResource(R.string.go_to_signup),
                style = BookOnTypography.caption.copy(textDecoration = TextDecoration.Underline),
                color = BookOnColor.TextPrimary,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnLoginScreenPreview() {
    BookOnTheme {
        BookOnLoginScreen(
            uiState = sampleLoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignupClick = {},
            onForgotPasswordClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnLoginErrorScreenPreview() {
    val baseState = sampleLoginUiState()
    BookOnTheme {
        BookOnLoginScreen(
            uiState = baseState.copy(
                email = baseState.email.copy(
                    value = "s20000@gsm.hs.kr",
                    errorText = stringResource(R.string.error_invalid_email_format),
                ),
                password = baseState.password.copy(
                    value = "12345",
                    errorText = stringResource(R.string.error_password_min_length),
                ),
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignupClick = {},
            onForgotPasswordClick = {},
        )
    }
}
