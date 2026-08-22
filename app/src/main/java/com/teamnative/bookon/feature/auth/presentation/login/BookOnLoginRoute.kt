package com.teamnative.bookon.feature.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

/** 서버 인증 전 로그인 입력 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnLoginRoute(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val viewModel: BookOnLoginViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val hasLoginError = state.hasMissingCredentials || state.password.errorText != null
    val passwordErrorText = if (state.hasMissingCredentials) {
        stringResource(R.string.error_login_credentials_required)
    } else {
        state.password.errorText
    }
    val uiState = state.copy(
        title = stringResource(R.string.login_title),
        email = state.email.copy(
            placeholder = stringResource(R.string.email_address),
            suffixText = stringResource(R.string.email_domain_gsm),
            isError = hasLoginError,
        ),
        password = state.password.copy(
            placeholder = stringResource(R.string.password),
            errorText = passwordErrorText,
        ),
        loginEnabled = !state.isSubmitting,
    )

    BookOnLoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onLoginClick = { viewModel.login(onLoginClick) },
        onSignupClick = onSignupClick,
        onForgotPasswordClick = onForgotPasswordClick,
    )
}
