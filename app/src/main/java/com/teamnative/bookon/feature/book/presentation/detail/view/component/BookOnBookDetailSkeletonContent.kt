package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnBookDetailSkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            BookOnSkeletonPlaceholder(
                Modifier.widthIn(max = AppComponentSize.BookDetailCoverWidth).fillMaxWidth().aspectRatio(2f / 3f),
            )
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.8f).height(AppSpacing.Large))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.5f).height(AppSpacing.Content))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
            repeat(3) {
                BookOnSkeletonPlaceholder(Modifier.weight(1f).height(AppComponentSize.BookDetailInfoMinHeight))
            }
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.4f).height(AppSpacing.Content))
        repeat(3) {
            BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppSpacing.Content))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailSkeletonContentPreview() {
    BookOnTheme {
        BookOnBookDetailSkeletonContent()
    }
}
