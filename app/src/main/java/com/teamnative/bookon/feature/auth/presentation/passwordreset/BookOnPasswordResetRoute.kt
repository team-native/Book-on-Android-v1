package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.component.BookOnPasswordPolicy

/** 비밀번호 재설정 입력값을 보존하고, UI 전용 단계 전환을 처리한다. */
@Composable
fun BookOnPasswordResetRoute(onNavigateBack: () -> Unit) {
    val viewModel: BookOnPasswordResetViewModel = hiltViewModel()
    val form by viewModel.state.collectAsStateWithLifecycle()
    val currentStep = form.step
    val email = form.email
    val verificationCode = form.code
    val password = form.password
    val passwordConfirm = form.confirm

    val uiState = defaultPasswordResetUiState().copy(
        step = currentStep,
        title = when (currentStep) {
            BookOnPasswordResetStep.Email -> stringResource(R.string.password_reset_email_title)
            BookOnPasswordResetStep.Verification -> stringResource(R.string.password_reset_verification_title)
            BookOnPasswordResetStep.Password -> stringResource(R.string.password_reset_new_password_title)
        },
        description = when (currentStep) {
            BookOnPasswordResetStep.Email -> stringResource(R.string.password_reset_email_description)
            BookOnPasswordResetStep.Verification -> stringResource(
                R.string.password_reset_verification_description,
                email,
            )
            BookOnPasswordResetStep.Password -> stringResource(R.string.password_reset_new_password_description)
        },
        email = BookOnTextFieldUiModel(
            value = email,
            label = stringResource(R.string.school_email),
            placeholder = stringResource(R.string.email_address),
        ),
        verificationCode = verificationCode,
        password = BookOnPasswordFieldUiModel(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            errorText = password.takeIf { it.isNotEmpty() && !BookOnPasswordPolicy.isValid(it) }
                ?.let { stringResource(R.string.error_password_rule) },
        ),
        passwordConfirm = BookOnPasswordFieldUiModel(
            value = passwordConfirm,
            label = stringResource(R.string.password_confirm),
            placeholder = stringResource(R.string.password_confirm),
            errorText = passwordConfirm.takeIf { it.isNotEmpty() && it != password }
                ?.let { stringResource(R.string.error_password_mismatch) },
        ),
        nextEnabled = when (currentStep) {
            BookOnPasswordResetStep.Email -> email.isNotBlank()
            BookOnPasswordResetStep.Verification -> verificationCode.length == 6
            BookOnPasswordResetStep.Password -> BookOnPasswordPolicy.isValid(password) && password == passwordConfirm
        },
        errorText = form.error,
        isLoading = form.isLoading,
    )

    BookOnPasswordResetScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                BookOnPasswordResetScreenEvent.BackClicked -> when (currentStep) {
                    BookOnPasswordResetStep.Email -> onNavigateBack()
                    BookOnPasswordResetStep.Verification -> viewModel.update { it.copy(step = BookOnPasswordResetStep.Email) }
                    BookOnPasswordResetStep.Password -> viewModel.update { it.copy(step = BookOnPasswordResetStep.Verification) }
                }

                is BookOnPasswordResetScreenEvent.EmailChanged -> viewModel.update { it.copy(email = event.email) }
                is BookOnPasswordResetScreenEvent.VerificationCodeChanged -> viewModel.update { it.copy(code = event.code) }
                is BookOnPasswordResetScreenEvent.PasswordChanged -> viewModel.update { it.copy(password = event.password) }
                is BookOnPasswordResetScreenEvent.PasswordConfirmChanged -> viewModel.update { it.copy(confirm = event.passwordConfirm) }
                BookOnPasswordResetScreenEvent.ResendClicked -> viewModel.resend()
                BookOnPasswordResetScreenEvent.ContinueClicked -> if (uiState.nextEnabled && !uiState.isLoading) viewModel.continueStep(onNavigateBack)
            }
        },
    )
}
