package com.teamnative.bookon.feature.library.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonBookCard
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnLibrarySkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.6f).height(AppSpacing.Large))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.5f).height(AppComponentSize.LibrarySortToggleHeight))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.ChipHeight))
        repeat(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content),
            ) {
                repeat(2) {
                    BookOnSkeletonBookCard(
                        modifier = Modifier.weight(1f),
                        coverWidth = AppComponentSize.LibraryBookCoverWidth,
                        coverHeight = AppComponentSize.LibraryBookCoverHeight,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnLibrarySkeletonContentPreview() {
    BookOnTheme {
        BookOnLibrarySkeletonContent()
    }
}
