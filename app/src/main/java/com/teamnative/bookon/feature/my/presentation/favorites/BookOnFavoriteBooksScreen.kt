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

/** 관심 도서 요약과 즐겨찾기 목록을 표시한다. */
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
                Text(
                    text = uiState.summary,
                    style = BookOnTypography.bodySemiBold,
                    color = BookOnColor.TextPrimary,
                )
            }
            items(uiState.books) { book ->
                BookOnBookListItem(
                    uiState = book,
                    onClick = onBookClick,
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
                                        onClick = {},
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
            onBookClick = {},
        )
    }
}
