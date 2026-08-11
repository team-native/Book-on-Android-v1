package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var code by remember { mutableStateOf("") }
    val uiState = defaultVerificationCodeUiState().copy(code = code, errorText = registrationState.errorMessage)

    BookOnVerificationCodeScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        // 서버 인증 전에도 사용자가 입력한 숫자를 화면 상태에 반영한다.
        onCodeChange = { code = it },
        onResendClick = viewModel::resendVerification,
        onConfirmClick = { viewModel.verify(code, onConfirmClick) },
        initialProgressStep = initialProgressStep,
    )
}
