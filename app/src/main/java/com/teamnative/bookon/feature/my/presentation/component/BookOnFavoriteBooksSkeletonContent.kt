package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonBookRow
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnFavoriteBooksSkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.7f).height(AppSpacing.Content))
        repeat(5) {
            BookOnSkeletonBookRow()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnFavoriteBooksSkeletonContentPreview() {
    BookOnTheme {
        BookOnFavoriteBooksSkeletonContent()
    }
}
