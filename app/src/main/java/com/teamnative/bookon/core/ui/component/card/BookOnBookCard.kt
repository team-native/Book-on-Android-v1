package com.teamnative.bookon.core.ui.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel

/**
 * 책 표지와 제목/저자 정보를 표시하는 공통 카드이다.
 * cover는 이미지 로더나 로컬 drawable을 연결할 수 있도록 slot으로 받는다.
 */
@Composable
fun BookOnBookCard(
    uiState: BookOnBookCardUiModel,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = BookOnTypography.bookTitle,
    coverWidth: Dp = AppComponentSize.BookCoverWidth,
    coverHeight: Dp = AppComponentSize.BookCoverHeight,
    cardWidth: Dp = coverWidth,
    cover: @Composable () -> Unit = { BookCoverPlaceholder(coverHeight) },
) {
    BookOnBookCard(
        title = uiState.title,
        author = uiState.author,
        modifier = modifier,
        cover = cover,
        titleStyle = titleStyle,
        coverWidth = coverWidth,
        coverHeight = coverHeight,
        cardWidth = cardWidth,
    )
}

/**
 * 책 표지와 제목/저자 정보를 표시하는 공통 카드이다.
 * cover는 이미지 로더나 로컬 drawable을 연결할 수 있도록 slot으로 받는다.
 */
@Composable
fun BookOnBookCard(
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = BookOnTypography.bookTitle,
    coverWidth: Dp = AppComponentSize.BookCoverWidth,
    coverHeight: Dp = AppComponentSize.BookCoverHeight,
    cardWidth: Dp = coverWidth,
    cover: @Composable () -> Unit = { BookCoverPlaceholder(coverHeight) },
) {
    Column(modifier = modifier.width(cardWidth)) {
        Box(
            modifier = Modifier
                .size(
                    width = coverWidth,
                    height = coverHeight,
                )
                .shadow(
                    elevation = AppElevation.BookCover,
                    shape = RoundedCornerShape(AppRadius.Small),
                )
                .clip(RoundedCornerShape(AppRadius.Small)),
            contentAlignment = Alignment.Center,
        ) {
            cover()
        }
        Spacer(modifier = Modifier.height(AppSpacing.Item))
        Text(
            text = title,
            style = titleStyle,
            color = BookOnColor.TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = author,
            style = BookOnTypography.bookMeta,
            color = BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun BookCoverPlaceholder(
    height: Dp = AppComponentSize.BookCoverHeight,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(BookOnColor.BookCoverPlaceholder),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookCardPreview() {
    BookOnTheme {
        BookOnBookCard(
            title = "자몽 살구 클럽",
            author = "한로로",
        )
    }
}
