package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthTopBar
import com.teamnative.bookon.feature.auth.presentation.component.BookOnEmailField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader

/** 비밀번호 재설정 첫 단계에서 학교 이메일을 입력받는다. */
@Composable
fun BookOnPasswordResetEmailScreen(
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
                text = stringResource(R.string.action_next),
                onClick = { onEvent(BookOnPasswordResetScreenEvent.ContinueClicked) },
                enabled = uiState.nextEnabled && !uiState.isLoading && !progressAnimating,
            )
        },
    ) {
        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))

        BookOnSignupStepHeader(
            step = PasswordResetEmailStep,
            title = uiState.title,
            description = uiState.description,
            onProgressAnimationRunningChange = { progressAnimating = it },
        )

        uiState.errorText?.let { errorText ->
            Text(
                text = errorText,
                style = BookOnTypography.bodyMedium,
                color = BookOnColor.TextSecondary,
            )
        }

        BookOnEmailField(
            uiState = uiState.email,
            onValueChange = { email ->
                onEvent(BookOnPasswordResetScreenEvent.EmailChanged(email))
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordResetEmailScreenPreview() {
    BookOnTheme {
        BookOnPasswordResetEmailScreen(
            uiState = defaultPasswordResetUiState(),
            onEvent = {},
        )
    }
}
