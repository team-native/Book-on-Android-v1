package com.teamnative.bookon.core.ui.component.book

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
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
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

/**
 * 검색 결과, 대출 내역, 즐겨찾기 목록에서 쓰는 책 정보 행이다.
 * cover는 실제 책 표지 로더를 호출부에서 연결할 수 있도록 slot으로 제공한다.
 */
@Composable
fun BookOnBookListItem(
    uiState: BookOnBookListItemUiModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    coverWidth: Dp = AppComponentSize.BookListCoverWidth,
    coverHeight: Dp = AppComponentSize.BookListCoverHeight,
    cover: @Composable () -> Unit = { BookListCoverPlaceholder() },
    trailingContent: (@Composable () -> Unit)? = null,
) {
    BookOnBookListItem(
        title = uiState.title,
        metaText = uiState.metaText,
        modifier = modifier,
        statusText = uiState.statusText,
        available = uiState.available,
        onClick = onClick,
        coverWidth = coverWidth,
        coverHeight = coverHeight,
        cover = cover,
        trailingContent = trailingContent,
    )
}

/**
 * 검색 결과, 대출 내역, 즐겨찾기 목록에서 쓰는 책 정보 행이다.
 * cover는 실제 책 표지 로더를 호출부에서 연결할 수 있도록 slot으로 제공한다.
 */
@Composable
fun BookOnBookListItem(
    title: String,
    metaText: String,
    modifier: Modifier = Modifier,
    statusText: String? = null,
    available: Boolean = true,
    onClick: (() -> Unit)? = null,
    coverWidth: Dp = AppComponentSize.BookListCoverWidth,
    coverHeight: Dp = AppComponentSize.BookListCoverHeight,
    cover: @Composable () -> Unit = { BookListCoverPlaceholder() },
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.BookListItemHeight)
            .shadow(
                elevation = AppElevation.Card,
                shape = RoundedCornerShape(AppRadius.Card),
            )
            .clip(RoundedCornerShape(AppRadius.Card))
            .background(BookOnColor.Surface)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = AppSpacing.Content, vertical = AppSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(coverWidth)
                .height(coverHeight)
                .clip(RoundedCornerShape(AppRadius.IconButton)),
            contentAlignment = Alignment.Center,
        ) {
            cover()
        }

        Spacer(modifier = Modifier.width(AppSpacing.Content))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Tiny))

            Text(
                text = metaText,
                style = BookOnTypography.caption,
                color = BookOnColor.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (statusText != null) {

            Spacer(modifier = Modifier.width(AppSpacing.Item))

            BookStatusBadge(
                text = statusText,
                available = available,
            )
        }
        if (trailingContent != null) {

            Spacer(modifier = Modifier.width(AppSpacing.Small))

            trailingContent()
        }
    }
}

@Composable
private fun BookStatusBadge(
    text: String,
    available: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppComponentSize.SmallChipHeight)
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(
                if (available) {
                    BookOnColor.StatusAvailableContainer
                } else {
                    BookOnColor.SurfaceAlt
                },
            )
            .padding(horizontal = AppSpacing.Item),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = BookOnTypography.badge,
            color = if (available) BookOnColor.PrimaryPressed else BookOnColor.TextPlaceholder,
            maxLines = 1,
        )
    }
}

@Composable
private fun BookListCoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BookOnColor.BookCoverSmallPlaceholder),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookListItemPreview() {
    BookOnTheme {
        BookOnBookListItem(
            title = "클린 코드",
            metaText = "로버트 C. 마틴 · 005.1",
            statusText = "재고 2권",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
