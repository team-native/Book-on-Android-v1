package com.teamnative.bookon.ui.Component.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppComponentSize
import com.teamnative.bookon.theme.AppElevation
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.home.BookOnPopularBookRowUiState

/**
 * 홈의 인기 책 영역처럼 작은 표지 placeholder와 책 정보를 가로 카드로 표시한다.
 * cover slot을 통해 실제 표지 로더를 연결할 수 있지만 기본값은 placeholder이다.
 */
@Composable
fun BookOnPopularBookRow(
    uiState: BookOnPopularBookRowUiState,
    modifier: Modifier = Modifier,
    cover: @Composable () -> Unit = { PopularBookCoverPlaceholder() },
) {
    BookOnPopularBookRow(
        title = uiState.title,
        metaText = uiState.metaText,
        modifier = modifier,
        statusText = uiState.statusText,
        cover = cover,
    )
}

/**
 * 홈의 인기 책 영역처럼 작은 표지 placeholder와 책 정보를 가로 카드로 표시한다.
 * cover slot을 통해 실제 표지 로더를 연결할 수 있지만 기본값은 placeholder이다.
 */
@Composable
fun BookOnPopularBookRow(
    title: String,
    metaText: String,
    modifier: Modifier = Modifier,
    statusText: String? = null,
    cover: @Composable () -> Unit = { PopularBookCoverPlaceholder() },
) {
    Row(
        modifier = modifier
            .width(206.dp)
            .height(76.dp)
            .shadow(
                elevation = AppElevation.Card,
                shape = RoundedCornerShape(AppRadius.Chip),
            )
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(BookOnColor.Surface)
            .padding(AppSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(AppComponentSize.PopularBookCoverWidth)
                .height(AppComponentSize.PopularBookCoverHeight)
                .clip(RoundedCornerShape(AppRadius.IconButton)),
            contentAlignment = Alignment.Center,
        ) {
            cover()
        }
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = BookOnTypography.caption,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Tiny))
            Text(
                text = metaText,
                style = BookOnTypography.bookMeta,
                color = BookOnColor.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (statusText != null) {
                Text(
                    text = statusText,
                    style = BookOnTypography.bookMeta,
                    color = BookOnColor.PrimaryPressed,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun PopularBookCoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BookOnColor.BookCoverSmallPlaceholder),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnPopularBookRowPreview() {
    BookOnTheme {
        BookOnPopularBookRow(
            title = "소년이 온다",
            metaText = "한강 · 재고 3권",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
