package com.teamnative.bookon.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.navigation.BookOnNavHost

/**
 * 앱의 최상위 Compose 진입점이다.
 * 화면 전환 정의는 navigation 패키지의 BookOnNavHost가 담당한다.
 */
@Composable
fun BookOnApp() {
    val sessionViewModel: BookOnSessionViewModel = hiltViewModel()
    val sessionUiState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    when (sessionUiState) {
        BookOnSessionUiState.Checking -> BookOnLoadingScreen()
        BookOnSessionUiState.Authenticated -> BookOnNavHost(isInitiallyAuthenticated = true)
        BookOnSessionUiState.Unauthenticated -> BookOnNavHost(isInitiallyAuthenticated = false)
    }
}
