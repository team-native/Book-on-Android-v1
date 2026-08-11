package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.home.presentation.component.BookOnBookSection
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeHeader
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeNoticeCard
import com.teamnative.bookon.feature.home.presentation.component.BookOnHomeSearchBar
import com.teamnative.bookon.feature.home.presentation.component.BookOnPopularBooksSection
import com.teamnative.bookon.feature.home.presentation.model.BookOnHomeNoticeUiModel

/** 홈 상단 액션, 검색, 공지, 추천과 인기 책 섹션을 조립한다. */
@Composable
fun BookOnHomeScreen(
    uiState: BookOnHomeScreenUiState,
    bottomBar: @Composable () -> Unit,
    onSearchClick: () -> Unit,
    onShowMoreClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fallbackNotice = BookOnHomeNoticeUiModel(
        category = stringResource(R.string.library_notice),
        dateText = stringResource(R.string.notice_date_author, "2026. 07. 01"),
        title = stringResource(R.string.summer_vacation_loan_notice_title),
        description = stringResource(R.string.summer_vacation_loan_notice_description),
        badgeText = stringResource(R.string.badge_new),
    )
    val notice = uiState.notice ?: fallbackNotice
    val userName = uiState.userName.takeIf { name -> name.isNotBlank() }?.let { name ->
        stringResource(R.string.user_name_suffix, name)
    }.orEmpty()

    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = BookOnColor.Background,
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
                    Text(text = errorMessage, color = BookOnColor.TextSecondary)
                    Button(onClick = onRetryClick) { Text(text = stringResource(R.string.action_retry)) }
                }
            }
            if (uiState.errorMessage == null && uiState.notice == null && uiState.aiRecommendedBooks.isEmpty() && uiState.popularBooks.isEmpty()) {
                item { Text(text = stringResource(R.string.empty_home), color = BookOnColor.TextSecondary) }
            }
            if (uiState.errorMessage == null) {
            item {
                BookOnHomeHeader(
                    greeting = uiState.greeting.ifBlank {
                        stringResource(R.string.home_greeting_evening)
                    },
                    userName = userName,
                    notificationContentDescription = stringResource(R.string.home_notification_description),
                    profileContentDescription = stringResource(R.string.home_profile_description),
                    onNotificationClick = onNotificationClick,
                )
            }
            item {
                BookOnHomeSearchBar(
                    placeholder = stringResource(R.string.home_search_placeholder),
                    searchContentDescription = stringResource(R.string.home_search_description),
                    onClick = onSearchClick,
                )
            }
            }
            item {
                BookOnHomeNoticeCard(
                    uiState = notice,
                    actionText = stringResource(R.string.action_view_detail),
                )
            }
            item {
                BookOnBookSection(
                    title = stringResource(R.string.ai_recommendation),
                    description = uiState.aiRecommendationDescription,
                    badgeText = stringResource(R.string.ai_recommendation_badge),
                    books = uiState.aiRecommendedBooks,
                    actionText = stringResource(R.string.action_show_more),
                    onActionClick = onShowMoreClick,
                )
            }
            item {
                BookOnPopularBooksSection(
                    title = stringResource(R.string.popular_books_school),
                    books = uiState.popularBooks,
                    actionText = stringResource(R.string.action_show_more),
                    onActionClick = onShowMoreClick,
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
            onSearchClick = {},
            onShowMoreClick = {},
            onNotificationClick = {},
            onRetryClick = {},
        )
    }
}
