package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.ui.component.card.BookOnBookCard
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.home.BookOnHomeSectionUiState

/**
 * 홈의 AI 추천 또는 일반 책 섹션을 제목 영역과 가로 책 카드 목록으로 조립한다.
 * 설명이나 배지가 있으면 AI 섹션 헤더를 사용하고, 없으면 일반 섹션 헤더를 사용한다.
 */
@Composable
fun BookOnBookSection(
    title: String,
    state: BookOnHomeSectionUiState<List<BookOnBookCardUiModel>>,
    emptyMessage: String,
    retryText: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    badgeText: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        if (description != null || badgeText != null) {
            BookOnHomeAiSectionHeader(
                title = title,
                description = description.orEmpty(),
                badgeText = badgeText,
                actionText = actionText,
                onActionClick = onActionClick,
            )
        } else {
            BookOnHomeSectionHeader(
                title = title,
                actionText = actionText,
                onActionClick = onActionClick,
            )
        }
        when (state) {
            is BookOnHomeSectionUiState.Content -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content)) {
                    items(state.value) { book ->
                        BookOnBookCard(
                            uiState = book,
                            coverWidth = AppComponentSize.HomeBookCoverWidth,
                            coverHeight = AppComponentSize.HomeBookCoverHeight,
                            cardWidth = AppComponentSize.HomeBookCardWidth,
                        )
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
