package com.teamnative.bookon.feature.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.component.BookOnPasswordPolicy

/** 서버 인증 전 로그인 입력 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnLoginRoute(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val uiState = sampleLoginUiState().copy(
        email = BookOnTextFieldUiModel(
            value = email,
            placeholder = stringResource(R.string.email_address),
            suffixText = stringResource(R.string.email_domain_gsm),
        ),
        password = BookOnPasswordFieldUiModel(
            value = password,
            placeholder = stringResource(R.string.password),
            errorText = password.takeIf { it.isNotEmpty() && !BookOnPasswordPolicy.isValid(it) }
                ?.let { stringResource(R.string.error_password_rule) },
        ),
        loginEnabled = email.isNotBlank() && BookOnPasswordPolicy.isValid(password),
    )

    BookOnLoginScreen(
        uiState = uiState,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = onLoginClick,
        onSignupClick = onSignupClick,
        onForgotPasswordClick = onForgotPasswordClick,
    )
}
