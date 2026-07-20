package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/** 서버 인증 전 인증번호 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnVerificationCodeRoute(
    initialProgressStep: Int?,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    var code by remember { mutableStateOf("") }
    val uiState = sampleVerificationCodeUiState().copy(code = code)

    BookOnVerificationCodeScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        // 서버 인증 전에도 사용자가 입력한 숫자를 화면 상태에 반영한다.
        onCodeChange = { code = it },
        onResendClick = {},
        onConfirmClick = onConfirmClick,
        initialProgressStep = initialProgressStep,
    )
}
