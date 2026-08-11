package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.component.BookOnBookSection
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeHeader
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeNoticeCard
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeSectionFeedback
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeSearchBar
import com.teamnative.bookon.feature.home.presentation.component.BookOnPopularBooksSection

/** 홈 상단 액션, 검색, 공지, 추천과 인기 책 섹션을 조립한다. */
@Composable
fun BookOnHomeScreen(
    uiState: BookOnHomeScreenUiState,
    bottomBar: @Composable () -> Unit,
    onEvent: (BookOnHomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(
                start = AppSpacing.HomeHorizontal,
                top = AppSpacing.ScreenVertical,
                end = AppSpacing.HomeHorizontal,
                bottom = AppSpacing.Section,
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            item {
                val userNameText = if (uiState.userName.isBlank()) {
                    ""
                } else {
                    stringResource(
                        R.string.user_name_suffix,
                        uiState.userName,
                    )
                }

                BookOnHomeHeader(
                    greeting = stringResource(R.string.home_greeting_evening),
                    userName = userNameText,
                    notificationContentDescription = stringResource(R.string.home_notification_description),
                    profileContentDescription = stringResource(R.string.home_profile_description),
                    onNotificationClick = {
                        onEvent(BookOnHomeScreenEvent.NotificationClicked)
                    },
                )
            }
            item {
                BookOnHomeSearchBar(
                    placeholder = stringResource(R.string.home_search_placeholder),
                    searchContentDescription = stringResource(R.string.home_search_description),
                    onClick = {
                        onEvent(BookOnHomeScreenEvent.SearchClicked)
                    },
                )
            }
            when (val noticeState = uiState.notice) {
                is BookOnHomeSectionUiState.Content -> {
                    item {
                        BookOnHomeNoticeCard(
                            uiState = noticeState.value,
                            actionText = stringResource(R.string.action_view_detail),
                        )
                    }
                }

                is BookOnHomeSectionUiState.Error -> {
                    item {
                        BookOnHomeSectionFeedback(
                            title = stringResource(R.string.library_notice),
                            message = noticeState.message,
                            retryText = stringResource(R.string.action_retry),
                            onRetryClick = {
                                onEvent(BookOnHomeScreenEvent.RetryNoticeClicked)
                            },
                        )
                    }
                }

                BookOnHomeSectionUiState.Empty -> {
                    item {
                        BookOnHomeNoticeCard(
                            category = stringResource(R.string.library_notice),
                            dateText = null,
                            title = stringResource(R.string.empty_notice_title),
                            description = stringResource(R.string.empty_notice_description),
                            iconContentDescription = stringResource(R.string.home_notification_description),
                        )
                    }
                }

                BookOnHomeSectionUiState.Loading -> Unit
            }
            item {
                val recommendationState = uiState.recommendation.toBookListState()
                val recommendationDescription = uiState.recommendation.descriptionOrEmpty()

                BookOnBookSection(
                    title = stringResource(R.string.ai_recommendation),
                    description = recommendationDescription,
                    badgeText = stringResource(R.string.ai_recommendation_badge),
                    state = recommendationState,
                    emptyMessage = stringResource(R.string.empty_home),
                    retryText = stringResource(R.string.action_retry),
                    onRetryClick = {
                        onEvent(BookOnHomeScreenEvent.RetryRecommendationClicked)
                    },
                    actionText = stringResource(R.string.action_show_more),
                    onActionClick = {
                        onEvent(BookOnHomeScreenEvent.ShowMoreClicked)
                    },
                )
            }
            item {
                BookOnPopularBooksSection(
                    title = stringResource(R.string.popular_books_school),
                    state = uiState.popularBooks,
                    emptyMessage = stringResource(R.string.empty_home),
                    retryText = stringResource(R.string.action_retry),
                    onRetryClick = {
                        onEvent(BookOnHomeScreenEvent.RetryPopularBooksClicked)
                    },
                    actionText = stringResource(R.string.action_show_more),
                    onActionClick = {
                        onEvent(BookOnHomeScreenEvent.ShowMoreClicked)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnHomeScreenPreview() {
    BookOnTheme {
        BookOnHomeScreen(
            uiState = sampleHomeUiState(),
            bottomBar = {},
            onEvent = {},
        )
    }
}

/** 홈 화면에서 발생한 사용자 의도를 Route로 전달한다. */
sealed interface BookOnHomeScreenEvent {
    data object SearchClicked : BookOnHomeScreenEvent

    data object ShowMoreClicked : BookOnHomeScreenEvent

    data object NotificationClicked : BookOnHomeScreenEvent

    data object RetryNoticeClicked : BookOnHomeScreenEvent

    data object RetryRecommendationClicked : BookOnHomeScreenEvent

    data object RetryPopularBooksClicked : BookOnHomeScreenEvent
}

private fun BookOnHomeSectionUiState<BookOnAiRecommendationUiModel>.toBookListState():
    BookOnHomeSectionUiState<List<BookOnBookCardUiModel>> = when (this) {
        is BookOnHomeSectionUiState.Content -> BookOnHomeSectionUiState.Content(value.books)
        BookOnHomeSectionUiState.Loading -> BookOnHomeSectionUiState.Loading
        BookOnHomeSectionUiState.Empty -> BookOnHomeSectionUiState.Empty
        is BookOnHomeSectionUiState.Error -> BookOnHomeSectionUiState.Error(message)
    }

private fun BookOnHomeSectionUiState<BookOnAiRecommendationUiModel>.descriptionOrEmpty(): String = when (this) {
    is BookOnHomeSectionUiState.Content -> value.description
    BookOnHomeSectionUiState.Loading,
    BookOnHomeSectionUiState.Empty,
    is BookOnHomeSectionUiState.Error,
    -> ""
}
