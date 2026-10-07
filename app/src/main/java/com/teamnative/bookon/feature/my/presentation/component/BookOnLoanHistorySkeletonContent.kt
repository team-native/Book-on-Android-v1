package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonBookRow
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnLoanHistorySkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.ChipHeight))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.5f).height(AppSpacing.Content))
        repeat(3) {
            BookOnSkeletonBookRow()
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.5f).height(AppSpacing.Content))
        repeat(2) {
            BookOnSkeletonBookRow()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnLoanHistorySkeletonContentPreview() {
    BookOnTheme {
        BookOnLoanHistorySkeletonContent()
    }
}
