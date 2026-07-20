package com.teamnative.bookon.feature.auth.presentation.passwordsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthPasswordField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthTopBar
import com.teamnative.bookon.feature.auth.presentation.component.BookOnPrivacyAgreementCard

/**
 * 비밀번호 설정 화면은 비밀번호 입력과 개인정보 동의 상태를 표시한다.
 */
@Composable
fun BookOnPasswordSetupScreen(
    uiState: BookOnPasswordSetupUiState,
    onBackClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onPrivacyCheckedChange: (Boolean) -> Unit,
    onPrivacyPolicyExpandedChange: (Boolean) -> Unit,
    onNextClick: () -> Unit,
    initialProgressStep: Int? = null,
    modifier: Modifier = Modifier,
) {
    var progressAnimating by remember { mutableStateOf(true) }

    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = {
            BookOnAuthTopBar(
                onBackClick = onBackClick,
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
                    step = 2,
                    title = uiState.title,
                    description = uiState.description,
                    initialProgressStep = initialProgressStep,
                    onProgressAnimationRunningChange = { progressAnimating = it },
                )
            }
            item {
                BookOnAuthPasswordField(
                    uiState = uiState.password,
                    onValueChange = onPasswordChange,
                )
            }
            item {
                BookOnAuthPasswordField(
                    uiState = uiState.passwordConfirm,
                    onValueChange = onPasswordConfirmChange,
                )
            }
            item {
                BookOnPrivacyAgreementCard(
                    checked = uiState.privacyChecked,
                    onCheckedChange = onPrivacyCheckedChange,
                    expanded = uiState.privacyPolicyExpanded,
                    onExpandedChange = onPrivacyPolicyExpandedChange,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordSetupScreenPreview() {
    BookOnTheme {
        BookOnPasswordSetupScreen(
            uiState = samplePasswordSetupUiState(),
            onBackClick = {},
            onPasswordChange = {},
            onPasswordConfirmChange = {},
            onPrivacyCheckedChange = {},
            onPrivacyPolicyExpandedChange = {},
            onNextClick = {},
        )
    }
}
