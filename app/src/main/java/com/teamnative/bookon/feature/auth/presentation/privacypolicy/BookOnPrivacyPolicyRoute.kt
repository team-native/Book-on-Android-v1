package com.teamnative.bookon.feature.auth.presentation.privacypolicy

import androidx.compose.runtime.Composable

/** 개인정보 안내 샘플 상태와 뒤로가기 이벤트를 연결한다. */
@Composable
fun BookOnPrivacyPolicyRoute(onBackClick: () -> Unit) {
    BookOnPrivacyPolicyScreen(
        uiState = samplePrivacyPolicyUiState(),
        onBackClick = onBackClick,
    )
}
