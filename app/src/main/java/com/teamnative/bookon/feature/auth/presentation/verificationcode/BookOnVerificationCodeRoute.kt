package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.runtime.Composable

/** 서버 인증 전 인증번호 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnVerificationCodeRoute(onBackClick: () -> Unit, onConfirmClick: () -> Unit) {
    BookOnVerificationCodeScreen(
        uiState = sampleVerificationCodeUiState(),
        onBackClick = onBackClick,
        onCodeChange = {},
        onResendClick = {},
        onConfirmClick = onConfirmClick,
    )
}
