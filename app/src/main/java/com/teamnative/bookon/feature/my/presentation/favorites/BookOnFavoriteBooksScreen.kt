package com.teamnative.bookon.feature.my.presentation.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.book.BookOnBookListItem
import com.teamnative.bookon.core.ui.component.loading.BookOnInlineLoadingIndicator
import com.teamnative.bookon.core.ui.model.resolve

/** 관심 도서 요약과 즐겨찾기 목록을 표시한다. */
@Composable
fun BookOnFavoriteBooksScreen(
    uiState: BookOnFavoriteBooksScreenUiState,
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit,
    onRetryClick: () -> Unit,
    onLoadMoreClick: () -> Unit,
    onRemoveFavoriteClick: (Long) -> Unit,
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
            uiState.errorMessage?.let { message ->
                item {
                    Text(text = message.resolve(), color = BookOnColor.TextSecondary)
                    Button(onClick = onRetryClick) { Text(text = stringResource(R.string.action_retry)) }
                }
            }
            if (uiState.errorMessage == null && uiState.books.isEmpty()) {
                item { Text(text = stringResource(R.string.empty_favorite_books), color = BookOnColor.TextSecondary) }
            }
            if (uiState.books.isNotEmpty()) item {
                Text(
                    text = uiState.summary,
                    style = BookOnTypography.bodySemiBold,
                    color = BookOnColor.TextPrimary,
                )
            }
            items(uiState.books) { book ->
                BookOnBookListItem(
                    uiState = book,
                    onClick = { onBookClick(book.id) },
                    trailingContent = if (book.isFavorite) {
                        {
                            val favoriteInteractionSource = remember { MutableInteractionSource() }
                            Box(
                                modifier = Modifier
                                    .size(AppIconSize.Default)
                                    .clickable(
                                        interactionSource = favoriteInteractionSource,
                                        indication = null,
                                        role = Role.Button,
                                        onClick = { onRemoveFavoriteClick(book.id) },
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.common_love),
                                    contentDescription = stringResource(R.string.favorite_book),
                                    modifier = Modifier.size(AppIconSize.Small),
                                )
                            }
                        }
                    } else {
                        null
                    },
                )
            }
            if (uiState.isPagingLoading) {
                item { BookOnInlineLoadingIndicator() }
            }
            if (uiState.hasNext && !uiState.isPagingLoading) {
                item {
                    Button(onClick = onLoadMoreClick) { Text(text = stringResource(R.string.action_load_more)) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnFavoriteBooksScreenPreview() {
    BookOnTheme {
        BookOnFavoriteBooksScreen(
            uiState = sampleFavoriteBooksUiState(),
            onBackClick = {},
            onBookClick = { _ -> },
            onRetryClick = {},
            onLoadMoreClick = {},
            onRemoveFavoriteClick = {},
        )
    }
}
