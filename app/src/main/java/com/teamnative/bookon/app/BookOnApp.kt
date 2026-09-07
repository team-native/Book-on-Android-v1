package com.teamnative.bookon.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.navigation.BookOnNavHost
import com.teamnative.bookon.navigation.BookOnPendingDeepLink

/**
 * 앱의 최상위 Compose 진입점이다.
 * 화면 전환 정의는 navigation 패키지의 BookOnNavHost가 담당한다.
 *
 * BookOnNavHost는 세션 상태가 처음 확정될 때(Checking → Authenticated/Unauthenticated) 딱 한 번만
 * 구성(compose)된다. 이후 로그인/로그아웃 전환은 BookOnNavHost 내부에서 back stack을 직접
 * clear+push하는 방식으로 처리하므로, 세션 상태가 바뀔 때마다 이 함수에서 NavHost를 다시 만들 필요가 없다.
 */
@Composable
internal fun BookOnApp(pendingDeepLink: BookOnPendingDeepLink? = null) {
    val sessionViewModel: BookOnSessionViewModel = hiltViewModel()
    val sessionUiState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    when (sessionUiState) {
        BookOnSessionUiState.Checking -> BookOnLoadingScreen()
        BookOnSessionUiState.Authenticated -> BookOnNavHost(
            isInitiallyAuthenticated = true,
            pendingDeepLink = pendingDeepLink,
            onLogout = sessionViewModel::logout,
        )
        BookOnSessionUiState.Unauthenticated -> BookOnNavHost(
            isInitiallyAuthenticated = false,
            pendingDeepLink = pendingDeepLink,
            onLogout = sessionViewModel::logout,
        )
        BookOnSessionUiState.RetryableError -> BookOnSessionRetryScreen(
            onRetryClick = sessionViewModel::retryAutoLogin,
            onLoginClick = sessionViewModel::logout,
        )
    }
}

/** 자동 로그인 확인이 일시적으로 실패했을 때 재시도 또는 로그인 진입을 제공한다. */
@Composable
private fun BookOnSessionRetryScreen(
    onRetryClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(R.string.session_restore_failed))
            Button(
                modifier = Modifier.padding(top = AppSpacing.Content),
                onClick = onRetryClick,
            ) {
                Text(text = stringResource(R.string.action_retry))
            }
            Button(
                modifier = Modifier.padding(top = AppSpacing.Item),
                onClick = onLoginClick,
            ) {
                Text(text = stringResource(R.string.action_go_to_login))
            }
        }
    }
}
