package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

/** Preview와 초기 Route 상태에서 사용하는 비밀번호 재설정 기본 표시값이다. */
@Composable
internal fun defaultPasswordResetUiState() = BookOnPasswordResetUiState(
    step = BookOnPasswordResetStep.Email,
    title = stringResource(R.string.password_reset_email_title),
    description = stringResource(R.string.password_reset_email_description),
    email = BookOnTextFieldUiModel(value = ""),
    verificationCode = "",
    password = BookOnPasswordFieldUiModel(value = ""),
    passwordConfirm = BookOnPasswordFieldUiModel(value = ""),
    nextEnabled = false,
)
