package com.teamnative.bookon.core.ui.component.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing

@Composable
fun BookOnSkeletonBookRow(
    modifier: Modifier = Modifier,
    rowHeight: Dp = AppComponentSize.BookListItemHeight,
    coverWidth: Dp = AppComponentSize.BookListCoverWidth,
    coverHeight: Dp = AppComponentSize.BookListCoverHeight,
) {
    Row(
        modifier = modifier.fillMaxWidth().height(rowHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        BookOnSkeletonPlaceholder(
            Modifier.size(
                width = coverWidth,
                height = coverHeight,
            ),
        )
        Column(
            modifier = Modifier.weight(1f).padding(vertical = AppSpacing.Small),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
        ) {
            BookOnSkeletonPlaceholder(Modifier.fillMaxWidth().height(AppSpacing.Content))
            BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.65f).height(AppSpacing.Item))
        }
    }
}
