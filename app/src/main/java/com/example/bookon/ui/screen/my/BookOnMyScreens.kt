package com.example.bookon.ui.screen.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.bookon.ui.commonComponent.book.BookOnBookListItem
import com.example.bookon.ui.commonComponent.button.BookOnLogoutButton
import com.example.bookon.ui.commonComponent.card.BookOnStatSummaryCard
import com.example.bookon.ui.commonComponent.chip.BookOnFilterChip
import com.example.bookon.ui.commonComponent.my.BookOnMyMarathonCard
import com.example.bookon.ui.commonComponent.my.BookOnMyProfileHeader
import com.example.bookon.ui.commonComponent.my.BookOnNotificationSettingsPanel
import com.example.bookon.ui.commonComponent.row.BookOnMenuRow
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.my.BookOnFavoriteBooksScreenUiState
import com.example.bookon.uiState.my.BookOnLoanHistoryScreenUiState
import com.example.bookon.uiState.my.BookOnMyScreenUiState
import com.example.bookon.uiState.my.defaultBookOnFavoriteBooksScreenUiState
import com.example.bookon.uiState.my.defaultBookOnLoanHistoryScreenUiState
import com.example.bookon.uiState.my.defaultBookOnMyScreenUiState

/**
 * 내 서재 Route는 전달받은 상태를 Screen에 연결한다.
 * 서버 연동 시 ViewModel에서 수집한 UiState를 uiState에 전달하면 화면 데이터가 갱신된다.
 */
@Composable
fun BookOnMyRoute(
    bottomBar: @Composable () -> Unit,
    uiState: BookOnMyScreenUiState = defaultBookOnMyScreenUiState(),
    onLoanHistoryClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    BookOnMyScreen(
        uiState = uiState,
        bottomBar = bottomBar,
        onMenuClick = { index ->
            when (index) {
                1 -> onLoanHistoryClick()
                2 -> onFavoriteClick()
            }
        },
        onLogoutClick = onLogoutClick,
        onNotificationChanged = { _, _ -> },
    )
}

/**
 * 내 서재 화면은 프로필, 통계, 독서마라톤, 메뉴, 알림 설정 패널을 표시한다.
 */
@Composable
fun BookOnMyScreen(
    uiState: BookOnMyScreenUiState,
    bottomBar: @Composable () -> Unit,
    onMenuClick: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    onNotificationChanged: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.my_library_title),
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item {
                BookOnMyProfileHeader(
                    userNameText = uiState.userNameText,
                    studentInfoText = uiState.studentInfoText,
                )
            }
            item {
                BookOnStatSummaryCard(items = uiState.stats)
            }
            item {
                BookOnMyMarathonCard(uiState = uiState.marathon)
            }
            item {
                Column {
                    uiState.menus.forEachIndexed { index, menu ->
                        BookOnMenuRow(
                            uiState = menu,
                            onClick = { onMenuClick(index) },
                        )
                    }
                }
            }
            item {
                BookOnLogoutButton(onClick = onLogoutClick)
            }
            item {
                BookOnNotificationSettingsPanel(
                    uiState = uiState.notificationPanel,
                    onCheckedChange = onNotificationChanged,
                )
            }
        }
    }
}

/**
 * 대출 반납 내역 Route는 상태 필터와 샘플 대출 목록을 Screen에 전달한다.
 */
@Composable
fun BookOnLoanHistoryRoute(
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    uiState: BookOnLoanHistoryScreenUiState = defaultBookOnLoanHistoryScreenUiState(),
) {
    BookOnLoanHistoryScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFilterClick = {},
        onBookClick = onBookClick,
    )
}

/**
 * 대출 반납 내역 화면은 대출 중과 지난 대출 목록을 분리해 표시한다.
 */
@Composable
fun BookOnLoanHistoryScreen(
    uiState: BookOnLoanHistoryScreenUiState,
    onBackClick: () -> Unit,
    onFilterClick: (Int) -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.loan_return_history),
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
            item {
                androidx.compose.foundation.layout.Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small),
                ) {
                    uiState.filters.forEachIndexed { index, filter ->
                        BookOnFilterChip(
                            uiState = filter,
                            onClick = { onFilterClick(index) },
                        )
                    }
                }
            }
            item {
                SectionTitle(text = uiState.currentTitle)
            }
            items(uiState.currentLoans) { book ->
                BookOnBookListItem(
                    uiState = book,
                    onClick = onBookClick,
                )
            }
            item {
                SectionTitle(text = uiState.pastTitle)
            }
            items(uiState.pastLoans) { book ->
                BookOnBookListItem(
                    uiState = book,
                    onClick = onBookClick,
                )
            }
        }
    }
}

/**
 * 즐겨찾기 Route는 서버 관심 도서 연동 전 샘플 목록을 Screen에 전달한다.
 */
@Composable
fun BookOnFavoriteBooksRoute(
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    uiState: BookOnFavoriteBooksScreenUiState = defaultBookOnFavoriteBooksScreenUiState(),
) {
    BookOnFavoriteBooksScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBookClick = onBookClick,
    )
}

/**
 * 즐겨찾기 화면은 관심 도서 요약과 책 목록을 표시한다.
 */
@Composable
fun BookOnFavoriteBooksScreen(
    uiState: BookOnFavoriteBooksScreenUiState,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.favorite_books),
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
            item {
                SectionTitle(text = uiState.summary)
            }
            items(uiState.books) { book ->
                BookOnBookListItem(
                    uiState = book,
                    onClick = onBookClick,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    androidx.compose.material3.Text(
        text = text,
        style = BookOnTypography.bodySemiBold,
        color = BookOnColor.TextPrimary,
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnMyScreenPreview() {
    BookOnTheme {
        BookOnMyScreen(
            uiState = defaultBookOnMyScreenUiState(),
            bottomBar = {},
            onMenuClick = {},
            onLogoutClick = {},
            onNotificationChanged = { _, _ -> },
        )
    }
}
