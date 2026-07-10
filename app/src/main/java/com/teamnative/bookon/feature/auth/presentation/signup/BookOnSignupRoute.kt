package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.runtime.Composable

/** 학교 정보 입력 단계의 샘플 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnSignupRoute(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    BookOnSignupScreen(
        uiState = sampleSignupUiState(),
        onBackClick = onBackClick,
        onPasswordNoticeClick = {},
        onEmailChange = {},
        onNameChange = {},
        onGenderSelected = {},
        onDepartmentClick = {},
        onNextClick = onNextClick,
    )
}
