package com.teamnative.bookon.feature.home.presentation.notices

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.feature.home.domain.HomeNotice

@Composable
fun BookOnNoticesRoute(
    onBackClick: () -> Unit,
    onNoticeClick: (HomeNotice) -> Unit,
    viewModel: BookOnNoticesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BookOnNoticesScreen(uiState) { event ->
        when (event) {
            BookOnNoticesScreenEvent.Back -> onBackClick()
            BookOnNoticesScreenEvent.Retry -> viewModel.retry()
            BookOnNoticesScreenEvent.LoadMore -> viewModel.loadNextPage()
            is BookOnNoticesScreenEvent.Open -> onNoticeClick(event.notice)
        }
    }
}

sealed interface BookOnNoticesScreenEvent {
    data object Back : BookOnNoticesScreenEvent
    data object Retry : BookOnNoticesScreenEvent
    data object LoadMore : BookOnNoticesScreenEvent
    data class Open(val notice: HomeNotice) : BookOnNoticesScreenEvent
}
