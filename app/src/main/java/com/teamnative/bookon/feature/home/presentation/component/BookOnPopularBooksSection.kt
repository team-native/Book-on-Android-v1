package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.feature.home.presentation.home.BookOnHomeSectionUiState
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel

/**
 * 홈 인기 책 영역을 섹션 헤더와 가로 인기 책 Row 목록으로 조립한다.
 * 더보기 액션은 호출 화면의 navigation callback으로 위임한다.
 */
@Composable
fun BookOnPopularBooksSection(
    title: String,
    state: BookOnHomeSectionUiState<List<BookOnPopularBookRowUiModel>>,
    emptyMessage: String,
    retryText: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        BookOnHomeSectionHeader(
            title = title,
            actionText = actionText,
            onActionClick = onActionClick,
        )
        when (state) {
            is BookOnHomeSectionUiState.Content -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
                    items(state.value) { book ->
                        BookOnPopularBookRow(uiState = book)
                    }
                }
            }

            BookOnHomeSectionUiState.Loading -> {
                BookOnHomeSectionFeedback()
            }

            BookOnHomeSectionUiState.Empty -> {
                BookOnHomeSectionFeedback(message = emptyMessage)
            }

            is BookOnHomeSectionUiState.Error -> {
                BookOnHomeSectionFeedback(
                    message = state.message,
                    retryText = retryText,
                    onRetryClick = onRetryClick,
                )
            }
        }
    }
}
