package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.feature.auth.presentation.signup.BookOnRegistrationViewModel

/** 서버 인증 전 인증번호 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnVerificationCodeRoute(
    initialProgressStep: Int?,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    viewModel: BookOnRegistrationViewModel = hiltViewModel(),
) {

    val registrationState by viewModel.state.collectAsStateWithLifecycle()
    androidx.lifecycle.compose.LifecycleResumeEffect(viewModel) {
        viewModel.updateRemainingTime()
        onPauseOrDispose { }
    }
    val code = registrationState.verificationCode
    val remaining = registrationState.remainingSeconds
    val uiState = defaultVerificationCodeUiState().copy(
        code = code,
        description = androidx.compose.ui.res.stringResource(
            com.teamnative.bookon.R.string.verification_code_description,
            registrationState.verificationEmail,
        ),
        expireText = androidx.compose.ui.res.stringResource(
            com.teamnative.bookon.R.string.verification_code_expire,
            "%02d:%02d".format(remaining / 60L, remaining % 60L),
        ),
        errorText = if (registrationState.hasInvalidDeadline) {
            androidx.compose.ui.res.stringResource(com.teamnative.bookon.R.string.error_verification_deadline)
        } else {
            registrationState.errorMessage
        },
        resendEnabled = !registrationState.isLoading,
        confirmEnabled = code.length == 6 && remaining > 0 && registrationState.sessionId != null && !registrationState.isLoading,
    )

    BookOnVerificationCodeScreen(
        uiState = uiState,
        onBackClick = {
            viewModel.cancelPendingRequest()
            onBackClick()
        },
        // 서버 인증 전에도 사용자가 입력한 숫자를 화면 상태에 반영한다.
        onCodeChange = viewModel::updateCode,
        onResendClick = viewModel::resendVerification,
        onConfirmClick = { viewModel.verify(code) },
        initialProgressStep = initialProgressStep,
    )
}
