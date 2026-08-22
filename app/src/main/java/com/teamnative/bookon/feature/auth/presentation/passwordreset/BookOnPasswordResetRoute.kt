package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.component.BookOnPasswordPolicy

/** 이메일 입력 화면의 상태와 이벤트를 비밀번호 재설정 ViewModel에 연결한다. */
@Composable
fun BookOnPasswordResetEmailRoute(
    onNavigateBack: () -> Unit,
    onNavigateToVerification: () -> Unit,
    viewModel: BookOnPasswordResetViewModel,
) {
    val form by viewModel.state.collectAsStateWithLifecycle()
    val uiState = defaultPasswordResetUiState().copy(
        title = stringResource(R.string.password_reset_email_title),
        description = stringResource(R.string.password_reset_email_description),
        email = BookOnTextFieldUiModel(
            value = form.email,
            label = stringResource(R.string.school_email),
            placeholder = stringResource(R.string.email_address),
        ),
        nextEnabled = form.email.isNotBlank(),
        errorText = form.error?.asText(),
        isLoading = form.isLoading,
    )

    BookOnPasswordResetEmailScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                BookOnPasswordResetScreenEvent.BackClicked -> onNavigateBack()
                is BookOnPasswordResetScreenEvent.EmailChanged -> viewModel.updateEmail(event.email)
                BookOnPasswordResetScreenEvent.ContinueClicked -> {
                    if (uiState.nextEnabled && !uiState.isLoading) {
                        viewModel.sendVerificationCode(onNavigateToVerification)
                    }
                }

                is BookOnPasswordResetScreenEvent.VerificationCodeChanged,
                is BookOnPasswordResetScreenEvent.PasswordChanged,
                is BookOnPasswordResetScreenEvent.PasswordConfirmChanged,
                BookOnPasswordResetScreenEvent.ResendClicked -> Unit
            }
        },
    )
}

/** 인증번호 입력 화면의 상태와 이벤트를 비밀번호 재설정 ViewModel에 연결한다. */
@Composable
fun BookOnPasswordResetVerificationRoute(
    onNavigateBack: () -> Unit,
    onNavigateToNewPassword: () -> Unit,
    viewModel: BookOnPasswordResetViewModel,
) {
    val form by viewModel.state.collectAsStateWithLifecycle()
    val uiState = defaultPasswordResetUiState().copy(
        title = stringResource(R.string.password_reset_verification_title),
        description = stringResource(
            R.string.password_reset_verification_description,
            form.email,
        ),
        verificationCode = form.code,
        nextEnabled = form.code.length == PasswordResetVerificationCodeLength,
        errorText = form.error?.asText(),
        isLoading = form.isLoading,
    )

    BookOnPasswordResetVerificationScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                BookOnPasswordResetScreenEvent.BackClicked -> onNavigateBack()
                is BookOnPasswordResetScreenEvent.VerificationCodeChanged -> {
                    viewModel.updateVerificationCode(event.code)
                }

                BookOnPasswordResetScreenEvent.ResendClicked -> {
                    if (!uiState.isLoading) viewModel.resendVerificationCode()
                }
                BookOnPasswordResetScreenEvent.ContinueClicked -> {
                    if (uiState.nextEnabled && !uiState.isLoading) {
                        onNavigateToNewPassword()
                    }
                }

                is BookOnPasswordResetScreenEvent.EmailChanged,
                is BookOnPasswordResetScreenEvent.PasswordChanged,
                is BookOnPasswordResetScreenEvent.PasswordConfirmChanged -> Unit
            }
        },
    )
}

/** 새 비밀번호 입력 화면의 상태와 이벤트를 비밀번호 재설정 ViewModel에 연결한다. */
@Composable
fun BookOnPasswordResetNewPasswordRoute(
    onNavigateBack: () -> Unit,
    onResetCompleted: () -> Unit,
    viewModel: BookOnPasswordResetViewModel,
) {
    val form by viewModel.state.collectAsStateWithLifecycle()
    val passwordError = form.password
        .takeIf { password ->
            password.isNotEmpty() && !BookOnPasswordPolicy.isValid(password)
        }
        ?.let {
            stringResource(R.string.error_password_rule)
        }
    val passwordConfirmError = form.confirm
        .takeIf { passwordConfirm ->
            passwordConfirm.isNotEmpty() && passwordConfirm != form.password
        }
        ?.let {
            stringResource(R.string.error_password_mismatch)
        }
    val uiState = defaultPasswordResetUiState().copy(
        title = stringResource(R.string.password_reset_new_password_title),
        description = stringResource(R.string.password_reset_new_password_description),
        password = BookOnPasswordFieldUiModel(
            value = form.password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            errorText = passwordError,
        ),
        passwordConfirm = BookOnPasswordFieldUiModel(
            value = form.confirm,
            label = stringResource(R.string.password_confirm_short),
            placeholder = stringResource(R.string.password_confirm),
            errorText = passwordConfirmError,
        ),
        nextEnabled = BookOnPasswordPolicy.isValid(form.password) && form.password == form.confirm,
        errorText = form.error?.asText(),
        isLoading = form.isLoading,
    )

    BookOnPasswordResetNewPasswordScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                BookOnPasswordResetScreenEvent.BackClicked -> onNavigateBack()
                is BookOnPasswordResetScreenEvent.PasswordChanged -> viewModel.updatePassword(event.password)
                is BookOnPasswordResetScreenEvent.PasswordConfirmChanged -> {
                    viewModel.updatePasswordConfirm(event.passwordConfirm)
                }

                BookOnPasswordResetScreenEvent.ContinueClicked -> {
                    if (uiState.nextEnabled && !uiState.isLoading) {
                        viewModel.resetPassword(onResetCompleted)
                    }
                }

                is BookOnPasswordResetScreenEvent.EmailChanged,
                is BookOnPasswordResetScreenEvent.VerificationCodeChanged,
                BookOnPasswordResetScreenEvent.ResendClicked -> Unit
            }
        },
    )
}

/** ViewModel의 재설정 오류를 화면에 표시할 리소스 문자열로 변환한다. */
@Composable
private fun PasswordResetError.asText(): String = when (this) {
    PasswordResetError.RequestFailed -> stringResource(R.string.error_password_reset_request_failed)
}
