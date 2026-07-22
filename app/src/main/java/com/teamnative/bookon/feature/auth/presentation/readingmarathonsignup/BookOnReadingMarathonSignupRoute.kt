package com.teamnative.bookon.feature.auth.presentation.readingmarathonsignup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** 독서마라톤 가입 안내 상태와 선택 이벤트를 연결한다. */
@Composable
fun BookOnReadingMarathonSignupRoute(
    onBackClick: () -> Unit,
    onUseClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    var isLinked by rememberSaveable { mutableStateOf(true) }

    BookOnReadingMarathonSignupScreen(
        uiState = defaultReadingMarathonSignupUiState().copy(isLinked = isLinked),
        onBackClick = onBackClick,
        onLinkChange = { isLinked = it },
        onContinueClick = onUseClick,
        onSkipClick = onSkipClick,
    )
}
