package com.teamnative.bookon.feature.auth.presentation.signupcomplete

import androidx.compose.runtime.Composable

/** 가입 완료 샘플 상태와 시작 이벤트를 연결한다. */
@Composable
fun BookOnSignupCompleteRoute(onStartClick: () -> Unit) {
    BookOnSignupCompleteScreen(
        uiState = sampleSignupCompleteUiState(),
        onStartClick = onStartClick,
    )
}
