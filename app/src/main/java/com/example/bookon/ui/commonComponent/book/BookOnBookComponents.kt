package com.example.bookon.ui.commonComponent.book

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bookon.ui.theme.AppComponentSize
import com.example.bookon.ui.theme.AppElevation
import com.example.bookon.ui.theme.AppRadius
import com.example.bookon.ui.theme.AppSpacing
import com.example.bookon.ui.theme.BookOnColor
import com.example.bookon.ui.theme.BookOnTheme
import com.example.bookon.ui.theme.BookOnTypography
import com.example.bookon.uiState.book.BookOnBookDetailInfoItemUiState
import com.example.bookon.uiState.book.BookOnBookDetailInfoRowUiState
import com.example.bookon.uiState.book.BookOnBookListItemUiState

/**
 * 검색 결과, 대출 내역, 즐겨찾기 목록에서 쓰는 책 정보 행이다.
 * cover는 실제 책 표지 로더를 호출부에서 연결할 수 있도록 slot으로 제공한다.
 */
@Composable
fun BookOnBookListItem(
    uiState: BookOnBookListItemUiState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    coverWidth: Dp = AppComponentSize.BookListCoverWidth,
    coverHeight: Dp = AppComponentSize.BookListCoverHeight,
    cover: @Composable () -> Unit = { BookListCoverPlaceholder() },
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
    }
}

/**
 * 상세 화면의 도서관 번호, 재고 수량, 대출 가능 여부 같은 정보를 같은 너비 타일로 표시한다.
 * items가 비어 있으면 아무것도 그리지 않는다.
 */
@Composable
fun BookOnBookDetailInfoRow(
    uiState: BookOnBookDetailInfoRowUiState,
    modifier: Modifier = Modifier,
) {
    BookOnBookDetailInfoRow(
        items = uiState.items,
        modifier = modifier,
    )
}

/**
 * 상세 화면의 도서관 번호, 재고 수량, 대출 가능 여부 같은 정보를 같은 너비 타일로 표시한다.
 * items가 비어 있으면 아무것도 그리지 않는다.
 */
@Composable
fun BookOnBookDetailInfoRow(
    items: List<BookOnBookDetailInfoItemUiState>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) {
        return
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        items.forEach { item ->
            BookOnBookDetailInfoItem(
                item = item,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/**
 * 상세 정보 행 내부의 단일 정보 타일이다.
 * highlighted는 대출 가능 여부처럼 주요 상태를 강조할 때 사용한다.
 */
@Composable
fun BookOnBookDetailInfoItem(
    item: BookOnBookDetailInfoItemUiState,
    modifier: Modifier = Modifier,
) {
    BookOnBookDetailInfoItem(
        label = item.label,
        value = item.value,
        modifier = modifier,
        highlighted = item.highlighted,
    )
}

/**
 * 상세 정보 행 내부의 단일 정보 타일이다.
 * highlighted는 대출 가능 여부처럼 주요 상태를 강조할 때 사용한다.
 */
@Composable
fun BookOnBookDetailInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
) {
    Column(
        modifier = modifier
            .height(68.dp)
            .clip(RoundedCornerShape(AppRadius.Small))
            .background(BookOnColor.SurfaceAlt)
            .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Item),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Tiny))
        Text(
            text = value,
            style = BookOnTypography.bodySemiBold,
            color = if (highlighted) BookOnColor.PrimaryPressed else BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
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

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailInfoRowPreview() {
    BookOnTheme {
        BookOnBookDetailInfoRow(
            items = listOf(
                BookOnBookDetailInfoItemUiState("도서관 번호", "000"),
                BookOnBookDetailInfoItemUiState("재고 수량", "2권"),
                BookOnBookDetailInfoItemUiState("대출 여부", "가능", highlighted = true),
            ),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
