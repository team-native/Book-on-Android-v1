package com.teamnative.bookon.feature.auth.presentation.readingmarathonsignup

import androidx.compose.runtime.Composable

/** 독서마라톤 가입 안내 상태와 선택 이벤트를 연결한다. */
@Composable
fun BookOnReadingMarathonSignupRoute(
    onBackClick: () -> Unit,
    onUseClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    BookOnReadingMarathonSignupScreen(
        uiState = sampleReadingMarathonSignupUiState(),
        onBackClick = onBackClick,
        onUseClick = onUseClick,
        onSkipClick = onSkipClick,
    )
}
