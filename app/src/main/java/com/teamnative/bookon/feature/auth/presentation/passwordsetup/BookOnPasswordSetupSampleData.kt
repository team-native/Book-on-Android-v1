package com.teamnative.bookon.feature.auth.presentation.passwordsetup

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel

@Composable
internal fun samplePasswordSetupUiState() = BookOnPasswordSetupUiState(
    stepText = stringResource(R.string.signup_step_2),
    title = stringResource(R.string.signup_account_info_title),
    description = stringResource(R.string.password_setup_description),
    password = BookOnPasswordFieldUiModel(
        value = "",
        label = stringResource(R.string.password),
        placeholder = stringResource(R.string.password),
    ),
    passwordConfirm = BookOnPasswordFieldUiModel(
        value = "",
        label = stringResource(R.string.password_confirm_short),
        placeholder = stringResource(R.string.password_confirm),
    ),
    ruleTitle = stringResource(R.string.password_precaution),
    ruleText = stringResource(R.string.password_rule),
    privacyChecked = false,
)
