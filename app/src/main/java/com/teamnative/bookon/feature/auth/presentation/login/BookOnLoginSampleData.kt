package com.teamnative.bookon.feature.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

@Composable
internal fun sampleLoginUiState() = BookOnLoginUiState(
    title = stringResource(R.string.login_title),
    email = BookOnTextFieldUiModel(
        value = "",
        placeholder = stringResource(R.string.email_address),
        suffixText = stringResource(R.string.email_domain_gsm),
    ),
    password = BookOnPasswordFieldUiModel("", placeholder = stringResource(R.string.password)),
)
