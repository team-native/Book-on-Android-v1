package com.teamnative.bookon.feature.book.presentation.detail.view

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenEvent
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailViewModel

// 도서 ID와 화면 이벤트를 상세 ViewModel에 연결한다.
@Composable
fun BookOnBookDetailRoute(
    bookId: Long,
    onBackClick: () -> Unit,
    viewModel: BookOnBookDetailViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val resources = LocalResources.current
    LaunchedEffect(bookId) {
        viewModel.load(bookId)
    }
    LifecycleResumeEffect(bookId) {
        viewModel.refreshOnResume(bookId)
        onPauseOrDispose { }
    }
    LaunchedEffect(viewModel, lifecycleOwner, resources) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect { message ->
                val text = when (message) {
                    is BookOnUiMessage.Dynamic -> message.value
                    is BookOnUiMessage.Resource -> resources.getString(
                        message.resId,
                        *message.formatArgs.toTypedArray(),
                    )
                }
                snackbarHostState.showSnackbar(text)
            }
        }
    }
    BookOnBookDetailScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = { event ->
            when (event) {
                BookOnBookDetailScreenEvent.BackClicked -> onBackClick()
                BookOnBookDetailScreenEvent.RetryClicked -> viewModel.load(bookId, forceRefresh = true)
                BookOnBookDetailScreenEvent.FavoriteClicked -> viewModel.toggleFavorite()
                BookOnBookDetailScreenEvent.LoanClicked -> viewModel.loan()
            }
        },
    )
}
