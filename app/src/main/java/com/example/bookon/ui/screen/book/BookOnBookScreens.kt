package com.example.bookon.ui.screen.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.R
import com.example.bookon.ui.commonComponent.bar.BookOnTopBar
import com.example.bookon.ui.commonComponent.book.BookOnBookDetailCover
import com.example.bookon.ui.commonComponent.book.BookOnBookDetailInfoRow
import com.example.bookon.ui.commonComponent.book.BookOnBookListItem
import com.example.bookon.ui.commonComponent.book.BookOnSearchBar
import com.example.bookon.ui.commonComponent.button.BookOnPrimaryButton
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.book.BookOnBookDetailInfoItemUiState
import com.example.bookon.uiState.book.BookOnBookDetailInfoRowUiState
import com.example.bookon.uiState.book.BookOnBookDetailScreenUiState
import com.example.bookon.uiState.book.BookOnBookListItemUiState
import com.example.bookon.uiState.book.BookOnSearchScreenUiState

/**
 * 검색 Route는 서버 검색 전까지 로컬 query와 샘플 검색 결과를 Screen에 전달한다.
 */
@Composable
fun BookOnSearchRoute(onBackClick: () -> Unit, onBookClick: () -> Unit) {
    var query by remember { mutableStateOf("클린") }
    val sample = previewSearchUiState(query = query, empty = query.isBlank())
    BookOnSearchScreen(
        uiState = sample,
        onQueryChange = { query = it },
        onBackClick = onBackClick,
        onBookClick = onBookClick,
    )
}

/**
 * 검색 화면은 검색 바, 결과 요약, 결과 목록 또는 빈 상태를 표시한다.
 */
@Composable
fun BookOnSearchScreen(
    uiState: BookOnSearchScreenUiState,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.action_search),
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
                BookOnSearchBar(
                    query = uiState.query,
                    placeholder = stringResource(R.string.search_book_placeholder),
                    onQueryChange = onQueryChange,
                )
            }
            if (uiState.books.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppSpacing.Section),
                        text = uiState.emptyMessage,
                        style = BookOnTypography.bodySemiBold,
                        color = BookOnColor.TextSecondary,
                    )
                }
            } else {
                item {
                    Text(
                        text = uiState.resultSummary,
                        style = BookOnTypography.caption,
                        color = BookOnColor.TextSecondary,
                    )
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
}

/**
 * 도서 상세 Route는 대출 가능 상태를 가진 샘플 상세 데이터를 Screen에 전달한다.
 */
@Composable
fun BookOnBookDetailRoute(onBackClick: () -> Unit) {
    BookOnBookDetailScreen(
        uiState = previewBookDetailUiState(loanAvailable = true),
        onBackClick = onBackClick,
        onLoanClick = {},
    )
}

/**
 * 도서 상세 화면은 서버 표지 영역을 비워두고 책 메타 정보와 소개를 표시한다.
 */
@Composable
fun BookOnBookDetailScreen(
    uiState: BookOnBookDetailScreenUiState,
    onBackClick: () -> Unit,
    onLoanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = "",
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
        bottomBar = {
            BookOnPrimaryButton(
                text = stringResource(
                    if (uiState.loanAvailable) {
                        R.string.loan_request
                    } else {
                        R.string.loan_unavailable
                    },
                ),
                onClick = onLoanClick,
                enabled = uiState.loanAvailable,
                modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(
                start = AppSpacing.ScreenHorizontal,
                end = AppSpacing.ScreenHorizontal,
                bottom = AppSpacing.Section,
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item {
                BookOnBookDetailCover()
            }
            item {
                Column {
                    Text(
                        text = uiState.title,
                        style = BookOnTypography.sectionTitle,
                        color = BookOnColor.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.Tiny))
                    Text(
                        text = uiState.author,
                        style = BookOnTypography.bodyMedium,
                        color = BookOnColor.TextSecondary,
                    )
                }
            }
            item {
                BookOnBookDetailInfoRow(uiState = uiState.info)
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
                    Text(
                        text = stringResource(R.string.book_intro),
                        style = BookOnTypography.bodySemiBold,
                        color = BookOnColor.TextPrimary,
                    )
                    Text(
                        text = uiState.intro,
                        style = BookOnTypography.bodyMedium,
                        color = BookOnColor.TextSecondary,
                    )
                }
            }
        }
    }
}

private fun previewSearchUiState(query: String = "클린", empty: Boolean = false): BookOnSearchScreenUiState {
    val books = if (empty) {
        emptyList()
    } else {
        listOf(
            BookOnBookListItemUiState("클린 코드", "로버트 C. 마틴 · 005.1", "재고 2권"),
            BookOnBookListItemUiState("클린 소프트웨어", "로버트 C. 마틴 · 005.1", "재고 3권"),
            BookOnBookListItemUiState("클린 코더", "로버트 C. 마틴 · 005.1", "재고 1권"),
            BookOnBookListItemUiState("클린 아키텍처", "로버트 C. 마틴 · 005.1", "재고 2권"),
        )
    }
    return BookOnSearchScreenUiState(
        query = query,
        resultSummary = "‘$query’ 검색 결과 ${books.size}건 · 제목 · 도서관 번호",
        books = books,
        emptyMessage = "검색된 책이 없어요\n다른 검색어를 입력해보세요",
    )
}

private fun previewBookDetailUiState(loanAvailable: Boolean) = BookOnBookDetailScreenUiState(
    title = "토마토 컵라면",
    author = "차정은",
    info = BookOnBookDetailInfoRowUiState(
        items = listOf(
            BookOnBookDetailInfoItemUiState("도서관 번호", "000"),
            BookOnBookDetailInfoItemUiState("재고 수량", if (loanAvailable) "2권" else "0권"),
            BookOnBookDetailInfoItemUiState("대출 여부", if (loanAvailable) "가능" else "불가", highlighted = loanAvailable),
        ),
    ),
    intro = "상처와 고민을 안고 살아가는 사람들이 우연한 만남을 통해 서로를 이해하고 위로받는 과정을 그린 이야기이다.",
    loanAvailable = loanAvailable,
)

@Preview(showBackground = true)
@Composable
private fun BookOnSearchScreenPreview() {
    BookOnTheme {
        BookOnSearchScreen(
            uiState = previewSearchUiState(),
            onQueryChange = {},
            onBackClick = {},
            onBookClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailScreenPreview() {
    BookOnTheme {
        BookOnBookDetailScreen(
            uiState = previewBookDetailUiState(loanAvailable = true),
            onBackClick = {},
            onLoanClick = {},
        )
    }
}
