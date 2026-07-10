package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnDropdownFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnOptionButtonUiModel

@Composable
internal fun sampleSignupUiState() = BookOnSignupUiState(
    stepText = stringResource(R.string.signup_step_1),
    title = stringResource(R.string.signup_school_info_title),
    description = stringResource(R.string.signup_school_account_description),
    email = BookOnTextFieldUiModel(
        value = "",
        label = stringResource(R.string.school_email),
        placeholder = stringResource(R.string.email_address),
        suffixText = stringResource(R.string.email_domain_gsm),
    ),
    name = BookOnTextFieldUiModel(
        value = "",
        label = stringResource(R.string.name),
        placeholder = stringResource(R.string.name),
    ),
    genderOptions = listOf(
        BookOnOptionButtonUiModel(stringResource(R.string.male), false),
        BookOnOptionButtonUiModel(stringResource(R.string.female), false),
    ),
    department = BookOnDropdownFieldUiModel("", stringResource(R.string.department)),
)
