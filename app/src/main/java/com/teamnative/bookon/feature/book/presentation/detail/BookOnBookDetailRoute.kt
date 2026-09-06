package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen

/** 서버 도서 상세 상태와 조회·대출·재시도 이벤트를 화면에 연결한다. */
@Composable
fun BookOnBookDetailRoute(
    bookId: Long,
    onBackClick: () -> Unit,
    viewModel: BookOnBookDetailViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(bookId) {
        viewModel.load(bookId)
    }

    if (state.isInitialLoading) {
        BookOnLoadingScreen()
    } else {
        state.content?.let { content ->
            BookOnBookDetailScreen(
                uiState = content,
                onBackClick = onBackClick,
                onLoanClick = viewModel::loan,
            )
        } ?: run {
            BookOnBookDetailStatusScreen(
                errorMessage = state.errorMessage,
                onBackClick = onBackClick,
                onRetryClick = {
                    viewModel.load(bookId)
                },
            )
        }
    }
}

/** 상세 응답 전 로딩과 실패 후 재시도만 표시하는 상태 화면이다. */
@Composable
private fun BookOnBookDetailStatusScreen(
    errorMessage: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            BookOnTopBar(
                title = "",
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = AppSpacing.Content,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            Text(
                text = errorMessage.orEmpty(),
                style = bookOnTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Button(onClick = onRetryClick) {
                Text(text = stringResource(R.string.action_retry))
            }
        }
    }
}
