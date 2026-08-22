package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthPasswordField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthTopBar
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader

/** 비밀번호 재설정 마지막 단계에서 새 비밀번호와 확인값을 입력받는다. */
@Composable
fun BookOnPasswordResetNewPasswordScreen(
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
                text = stringResource(R.string.action_reset_password),
                onClick = { onEvent(BookOnPasswordResetScreenEvent.ContinueClicked) },
                enabled = uiState.nextEnabled && !uiState.isLoading && !progressAnimating,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = AuthTitleTopSpacing,
                bottom = AppSpacing.Section,
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item {
                BookOnSignupStepHeader(
                    step = PasswordResetNewPasswordStep,
                    title = uiState.title,
                    description = uiState.description,
                    onProgressAnimationRunningChange = { progressAnimating = it },
                )
            }
            uiState.errorText?.let { errorText ->
                item {
                    Text(
                        text = errorText,
                        style = bookOnTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            item {
                BookOnAuthPasswordField(
                    uiState = uiState.password,
                    onValueChange = { password ->
                        onEvent(BookOnPasswordResetScreenEvent.PasswordChanged(password))
                    },
                )
            }
            item {
                BookOnAuthPasswordField(
                    uiState = uiState.passwordConfirm,
                    onValueChange = { passwordConfirm ->
                        onEvent(
                            BookOnPasswordResetScreenEvent.PasswordConfirmChanged(
                                passwordConfirm,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordResetNewPasswordScreenPreview() {
    BookOnTheme {
        BookOnPasswordResetNewPasswordScreen(
            uiState = defaultPasswordResetUiState().copy(
                title = stringResource(R.string.password_reset_new_password_title),
                description = stringResource(R.string.password_reset_new_password_description),
            ),
            onEvent = {},
        )
    }
}
