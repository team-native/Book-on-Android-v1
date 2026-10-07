package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnMySkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content)) {
            BookOnSkeletonPlaceholder(
                modifier = Modifier.size(AppIconSize.Avatar),
                shape = CircleShape,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
            ) {
                BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.7f).height(AppSpacing.Content))
                BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.9f).height(AppSpacing.Content))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
            repeat(3) {
                BookOnSkeletonPlaceholder(Modifier.weight(1f).height(AppComponentSize.StatSummaryCardHeight))
            }
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.StatSummaryCardHeight * 2))
        repeat(4) {
            BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.MenuRowHeight))
        }
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.4f).height(AppComponentSize.ButtonHeight))
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMySkeletonContentPreview() {
    BookOnTheme {
        BookOnMySkeletonContent()
    }
}
