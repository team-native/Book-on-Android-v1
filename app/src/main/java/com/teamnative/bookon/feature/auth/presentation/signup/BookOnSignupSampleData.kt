package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnDropdownFieldUiModel

@Composable
internal fun defaultSignupUiState(
    email: String = "",
    name: String = "",
    selectedGender: BookOnGender? = null,
    selectedDepartmentText: String = "",
    isDepartmentMenuExpanded: Boolean = false,
) = BookOnSignupUiState(
    stepText = stringResource(R.string.signup_step_1),
    title = stringResource(R.string.signup_school_info_title),
    description = stringResource(R.string.signup_school_account_description),
    email = BookOnTextFieldUiModel(
        value = email,
        label = stringResource(R.string.school_email),
        placeholder = stringResource(R.string.email_address),
        suffixText = stringResource(R.string.email_domain_gsm),
    ),
    name = BookOnTextFieldUiModel(
        value = name,
        label = stringResource(R.string.name),
        placeholder = stringResource(R.string.name),
    ),
    selectedGender = selectedGender,
    department = BookOnDropdownFieldUiModel(
        text = selectedDepartmentText,
        placeholder = stringResource(R.string.department),
        expanded = isDepartmentMenuExpanded,
    ),
)
