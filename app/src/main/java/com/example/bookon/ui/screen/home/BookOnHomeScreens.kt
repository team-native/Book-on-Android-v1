package com.example.bookon.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.R
import com.example.bookon.ui.commonComponent.bar.BookOnTopBar
import com.example.bookon.ui.commonComponent.card.BookOnBookCard
import com.example.bookon.ui.commonComponent.home.BookOnBookSection
import com.example.bookon.ui.commonComponent.home.BookOnHomeHeader
import com.example.bookon.ui.commonComponent.home.BookOnHomeNoticeCard
import com.example.bookon.ui.commonComponent.home.BookOnHomeSearchBar
import com.example.bookon.ui.commonComponent.home.BookOnPopularBooksSection
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.uiState.BookOnBookCardUiState
import com.example.bookon.uiState.home.BookOnHomeNoticeUiState
import com.example.bookon.uiState.home.BookOnHomeScreenUiState
import com.example.bookon.uiState.home.BookOnNewBooksScreenUiState
import com.example.bookon.uiState.home.BookOnPopularBookRowUiState

/**
 * 홈 Route는 서버 연동 전 샘플 UiState를 Screen에 전달한다.
 * 이후 ViewModel이 생기면 이 함수에서 StateFlow를 수집하도록 교체한다.
 */
@Composable
fun BookOnHomeRoute(
    bottomBar: @Composable () -> Unit,
    onSearchClick: () -> Unit,
    onNewBooksClick: () -> Unit,
) {
    BookOnHomeScreen(
        uiState = previewHomeUiState(),
        bottomBar = bottomBar,
        onSearchClick = onSearchClick,
        onShowMoreClick = onNewBooksClick,
    )
}

/**
 * 홈 화면은 피그마 메인 화면 순서에 맞춰 상단 액션, 검색, 공지, AI 추천, 인기 책을 조립한다.
 */
@Composable
fun BookOnHomeScreen(
    uiState: BookOnHomeScreenUiState,
    bottomBar: @Composable () -> Unit,
    onSearchClick: () -> Unit,
    onShowMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            item {
                BookOnHomeHeader(
                    greeting = uiState.greeting,
                    userName = uiState.userName,
                    notificationContentDescription = stringResource(R.string.home_notification_description),
                    profileContentDescription = stringResource(R.string.home_profile_description),
                )
            }
            item {
                BookOnHomeSearchBar(
                    placeholder = stringResource(R.string.home_search_placeholder),
                    searchContentDescription = stringResource(R.string.home_search_description),
                    onClick = onSearchClick,
                )
            }
            item {
                BookOnHomeNoticeCard(
                    uiState = uiState.notice,
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

/**
 * 신간 추천 Route는 홈의 신간 섹션과 같은 책 카드 컴포넌트를 사용한다.
 */
@Composable
fun BookOnNewBooksRoute(onBackClick: () -> Unit) {
    BookOnNewBooksScreen(
        uiState = BookOnNewBooksScreenUiState(books = previewHomeUiState().newBooks),
        onBackClick = onBackClick,
    )
}

/**
 * 신간 추천 화면은 서버 책 목록이 들어오기 전 샘플 카드 목록을 표시한다.
 */
@Composable
fun BookOnNewBooksScreen(
    uiState: BookOnNewBooksScreenUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.new_books_recent),
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            items(uiState.books.chunked(2)) { rowBooks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    rowBooks.forEach { book ->
                        BookOnBookCard(uiState = book)
                    }
                    if (rowBooks.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

private fun previewHomeUiState() = BookOnHomeScreenUiState(
    greeting = "좋은 저녁이에요",
    userName = "홍길동님",
    notice = BookOnHomeNoticeUiState(
        category = "도서부 공지",
        dateText = "2026. 07. 01 · 도서부",
        title = "여름방학 도서 대출 기간 연장 안내",
        description = "방학 기간 동안 1인당 최대 5권, 대출 기간이 14일로 연장됩니다.",
        badgeText = "NEW",
    ),
    aiRecommendationDescription = "홍길동님의 대출 이력을 분석해 골랐어요",
    aiRecommendedBooks = listOf(
        BookOnBookCardUiState("나미야 잡화점의 기적", "히가시노 게이고"),
        BookOnBookCardUiState("아몬드", "손원평"),
        BookOnBookCardUiState("오늘 밤, 세계에서 이 사랑이 사라진다 해도", "이치조 미사키"),
    ),
    popularBooks = listOf(
        BookOnPopularBookRowUiState("소년이 온다", "한강 · 재고 3권"),
        BookOnPopularBookRowUiState("데미안", "헤르만 헤세 · 재고 1권"),
    ),
    newBooks = listOf(
        BookOnBookCardUiState("자몽 살구 클럽", "한로로"),
        BookOnBookCardUiState("괴테는 모든 것을 말했다", "스즈키 유이"),
        BookOnBookCardUiState("혼모노", "성해나"),
        BookOnBookCardUiState("사랑하는 겉들", "이옥토"),
    ),
)

@Preview(showBackground = true)
@Composable
private fun BookOnHomeScreenPreview() {
    BookOnTheme {
        BookOnHomeScreen(
            uiState = previewHomeUiState(),
            bottomBar = {},
            onSearchClick = {},
            onShowMoreClick = {},
        )
    }
}
