package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.home.presentation.component.BookOnBookSection
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeHeader
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeNoticeCard
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
    val userName = uiState.userName.takeIf { name -> name.isNotBlank() }?.let { name ->
        stringResource(R.string.user_name_suffix, name)
    }.orEmpty()

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
            uiState.errorMessage?.let { errorMessage ->
                item {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Button(
                        onClick = {
                            onEvent(BookOnHomeScreenEvent.RetryClicked)
                        },
                    ) {
                        Text(text = stringResource(R.string.action_retry))
                    }
                }
            }
            // 공지 카드는 데이터가 없어도 기본 형식으로 항상 노출되므로, 별도의 "표시할 홈 정보가 없어요" 전체 빈 상태는 더 이상 필요하지 않다.
            // 특정 섹션(공지 등)이 실패해도 상단 탐색 UI는 항상 노출한다.
            item {
                BookOnHomeHeader(
                    greeting = uiState.greeting.ifBlank {
                        stringResource(R.string.home_greeting_evening)
                    },
                    userName = userName,
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
            item {
                val notice = uiState.notice
                if (notice != null) {
                    BookOnHomeNoticeCard(
                        uiState = notice,
                        actionText = stringResource(R.string.action_view_detail),
                    )
                } else {
                    // 공지 조회가 실패했거나 등록된 공지가 없어도 공지 카드의 기본 형식은 유지한다.
                    BookOnHomeNoticeCard(
                        category = stringResource(R.string.library_notice),
                        dateText = null,
                        title = stringResource(R.string.empty_notice_title),
                        description = stringResource(R.string.empty_notice_description),
                    )
                }
            }
            item {
                BookOnBookSection(
                    title = stringResource(R.string.ai_recommendation),
                    description = uiState.aiRecommendationDescription,
                    badgeText = stringResource(R.string.ai_recommendation_badge),
                    books = uiState.aiRecommendedBooks,
                    onBookClick = { bookId ->
                        onEvent(BookOnHomeScreenEvent.BookClicked(bookId))
                    },
                )
            }
            item {
                BookOnPopularBooksSection(
                    title = stringResource(R.string.popular_books_school),
                    books = uiState.popularBooks,
                    actionText = stringResource(R.string.action_show_more),
                    onActionClick = {
                        onEvent(BookOnHomeScreenEvent.PopularBooksMoreClicked)
                    },
                    onBookClick = { bookId ->
                        onEvent(BookOnHomeScreenEvent.BookClicked(bookId))
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
