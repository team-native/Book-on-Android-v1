package com.teamnative.bookon.feature.home.presentation.newbooks.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonBookCard
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout

@Composable
fun BookOnNewBooksSkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        repeat(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content),
            ) {
                repeat(2) {
                    BookOnSkeletonBookCard(
                        modifier = Modifier.weight(1f),
                        coverWidth = AppComponentSize.BookCoverWidth,
                        coverHeight = AppComponentSize.BookCoverHeight,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnNewBooksSkeletonContentPreview() {
    BookOnTheme {
        BookOnNewBooksSkeletonContent()
    }
}
