package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen



/**
 * 도서실 Route는 ViewModel의 서버 목록 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnLibraryRoute(
    bottomBar: @Composable () -> Unit,
    onBookClick: (Long) -> Unit,
    viewModel: BookOnLibraryViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        BookOnLibraryScreen(
            uiState = uiState,
            bottomBar = bottomBar,
            onEvent = viewModel::onEvent,
            onBookClick = onBookClick,
        )
    }
}
