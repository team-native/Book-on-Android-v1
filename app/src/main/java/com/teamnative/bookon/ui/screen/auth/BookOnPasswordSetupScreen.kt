package com.teamnative.bookon.ui.screen.auth

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
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.ui.Component.auth.BookOnAuthFormScaffold
import com.teamnative.bookon.ui.Component.auth.BookOnSignupStepHeader
import com.teamnative.bookon.ui.Component.button.BookOnPrimaryButton
import com.teamnative.bookon.uiState.auth.BookOnPasswordSetupUiState

/**
 * 비밀번호 설정 화면은 비밀번호 입력과 개인정보 동의 상태를 표시한다.
 */
@Composable
fun BookOnPasswordSetupScreen(
    uiState: BookOnPasswordSetupUiState,
    onBackClick: () -> Unit,
    onPasswordNoticeClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onPrivacyCheckedChange: (Boolean) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var progressAnimating by remember { mutableStateOf(true) }

    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = {
            BookOnAuthTopBar(
                onBackClick = onBackClick,
                onPasswordNoticeClick = onPasswordNoticeClick,
            )
        },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_next),
                onClick = onNextClick,
                enabled = uiState.nextEnabled && !progressAnimating,
            )
        },
    ) {
        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))
        BookOnSignupStepHeader(
            step = 2,
            title = uiState.title,
            description = uiState.description,
            onProgressAnimationRunningChange = { progressAnimating = it },
        )
        BookOnAuthPasswordField(uiState = uiState.password, onValueChange = onPasswordChange)
        BookOnAuthPasswordField(uiState = uiState.passwordConfirm, onValueChange = onPasswordConfirmChange)
        BookOnPrivacyAgreementCard(
            checked = uiState.privacyChecked,
            onCheckedChange = onPrivacyCheckedChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordSetupScreenPreview() {
    BookOnTheme {
        BookOnPasswordSetupScreen(
            uiState = previewPasswordSetupUiState(),
            onBackClick = {},
            onPasswordNoticeClick = {},
            onPasswordChange = {},
            onPasswordConfirmChange = {},
            onPrivacyCheckedChange = {},
            onNextClick = {},
        )
    }
}
