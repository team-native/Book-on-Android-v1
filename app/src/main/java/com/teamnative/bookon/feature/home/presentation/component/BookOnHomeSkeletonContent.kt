package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonBookCard
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonBookRow
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnHomeSkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(
        modifier = modifier,
        horizontalPadding = AppSpacing.HomeHorizontal,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
            ) {
                BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.6f).height(AppSpacing.Content))
                BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.8f).height(AppSpacing.Large))
            }
            BookOnSkeletonPlaceholder(
                modifier = Modifier.size(AppIconSize.Avatar),
                shape = CircleShape,
            )
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.HomeSearchHeight))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.StatSummaryCardHeight))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.5f).height(AppSpacing.Content))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content)) {
            items(3) {
                BookOnSkeletonBookCard(
                    modifier = Modifier.width(AppComponentSize.HomeBookCardWidth),
                    coverWidth = AppComponentSize.HomeBookCoverWidth,
                    coverHeight = AppComponentSize.HomeBookCoverHeight,
                )
            }
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.5f).height(AppSpacing.Content))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
            items(2) {
                BookOnSkeletonBookRow(
                    modifier = Modifier.width(POPULAR_CARD_WIDTH),
                    rowHeight = POPULAR_CARD_HEIGHT,
                    coverWidth = AppComponentSize.PopularBookCoverWidth,
                    coverHeight = AppComponentSize.PopularBookCoverHeight,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnHomeSkeletonContentPreview() {
    BookOnTheme {
        BookOnHomeSkeletonContent()
    }
}

private val POPULAR_CARD_WIDTH = 206.dp
private val POPULAR_CARD_HEIGHT = 76.dp
