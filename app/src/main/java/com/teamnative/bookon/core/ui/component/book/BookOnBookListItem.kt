package com.teamnative.bookon.core.ui.component.book

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

/**
 * 검색 결과, 대출 내역, 즐겨찾기 목록에서 쓰는 책 정보 행이다.
 * 서버 표지 URL이 없거나 로드에 실패하면 기존 placeholder를 표시한다.
 */
@Composable
fun BookOnBookListItem(
    uiState: BookOnBookListItemUiModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    coverWidth: Dp = AppComponentSize.BookListCoverWidth,
    coverHeight: Dp = AppComponentSize.BookListCoverHeight,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    BookOnBookListItem(
        title = uiState.title,
        metaText = uiState.metaText,
        modifier = modifier,
        statusText = uiState.statusText,
        available = uiState.available,
        coverImageUrl = uiState.coverImageUrl,
        onClick = onClick,
        coverWidth = coverWidth,
        coverHeight = coverHeight,
        trailingContent = trailingContent,
    )
}

/**
 * 검색 결과, 대출 내역, 즐겨찾기 목록에서 쓰는 책 정보 행이다.
 * 서버 표지 URL이 없거나 로드에 실패하면 기존 placeholder를 표시한다.
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
    coverImageUrl: String? = null,
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
            .background(MaterialTheme.colorScheme.surface)
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
            BookOnRemoteBookCover(
                coverImageUrl = coverImageUrl,
                placeholderColor = MaterialTheme.colorScheme.tertiaryContainer,
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Content))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = bookOnTypography.bodySemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Tiny))

            Text(
                text = metaText,
                style = bookOnTypography.caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
            )
            .padding(horizontal = AppSpacing.Item),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = bookOnTypography.badge,
            color = if (available) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
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
