package com.teamnative.bookon.feature.book.presentation.detail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailActions
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailCover
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailHeading
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailIntroduction
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailMetadata
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailStatus
import com.teamnative.bookon.feature.book.presentation.detail.view.component.BookOnBookDetailTopBar
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenEvent
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailState
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.sampleBookDetailUiState

// 모든 상세 상태에서 뒤로가기와 고정 액션을 제공한다.
@Composable
fun BookOnBookDetailScreen(
    state: BookOnBookDetailState,
    onEvent: (BookOnBookDetailScreenEvent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    val content = state.content
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        if (MaterialTheme.colorScheme.surface.luminance() > 0.5f) {
            Image(
                painter = painterResource(R.drawable.book_detail_glow),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(392f / 360f),
            )
        }
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                BookOnBookDetailTopBar(
                    onBackClick = { onEvent(BookOnBookDetailScreenEvent.BackClicked) },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (content != null) {
                    BookOnBookDetailActions(
                        uiState = content,
                        isRefreshing = state.isRefreshing,
                        onFavoriteClick = { onEvent(BookOnBookDetailScreenEvent.FavoriteClicked) },
                    )
                }
            },
        ) { innerPadding ->
            if (content == null) {
                BookOnBookDetailStatus(
                    isLoading = state.isInitialLoading,
                    errorMessage = state.errorMessage,
                    onRetryClick = { onEvent(BookOnBookDetailScreenEvent.RetryClicked) },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(AppSpacing.ScreenHorizontal),
                )
            } else {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .testTag("book_detail_content"),
                    contentPadding =
                        PaddingValues(
                            start = AppSpacing.ScreenHorizontal,
                            end = AppSpacing.ScreenHorizontal,
                            top = AppSpacing.Small,
                            bottom = AppSpacing.Section,
                        ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
                ) {
                    if (state.isRefreshing) {
                        item { BookOnInlineLoadingIndicator() }
                    }
                    if (state.errorMessage != null) {
                        item {
                            BookOnBookDetailStatus(
                                isLoading = false,
                                errorMessage = state.errorMessage,
                                onRetryClick = { onEvent(BookOnBookDetailScreenEvent.RetryClicked) },
                            )
                        }
                    }
                    item {
                        BookOnBookDetailCover(coverImageUrl = content.coverImageUrl)
                    }
                    item {
                        BookOnBookDetailHeading(
                            title = content.title,
                            author = content.author,
                            modifier =
                                Modifier.padding(
                                    top = AppSpacing.BookDetailCoverToTitle - AppSpacing.Content,
                                ),
                        )
                    }
                    item {
                        BookOnBookDetailMetadata(
                            libraryNumber = content.libraryNumber,
                            totalQuantity = content.totalQuantity,
                            availableQuantity = content.availableQuantity,
                            loanAvailable = content.loanAvailable,
                        )
                    }
                    item {
                        BookOnBookDetailIntroduction(
                            intro = content.intro,
                            modifier =
                                Modifier.padding(
                                    top = AppSpacing.BookDetailIntroTop - AppSpacing.Content,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailScreenPreview() {
    BookOnTheme {
        BookOnBookDetailScreen(
            state =
                BookOnBookDetailState(
                    content = sampleBookDetailUiState(true),
                    isInitialLoading = false,
                ),
            onEvent = {},
        )
    }
}
