package com.teamnative.bookon.core.ui.component.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing

@Composable
fun BookOnSkeletonBookCard(
    modifier: Modifier = Modifier,
    coverWidth: Dp = AppComponentSize.BookCoverWidth,
    coverHeight: Dp = AppComponentSize.BookCoverHeight,
) {
    Column(
        modifier = modifier.widthIn(max = coverWidth),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
    ) {
        BookOnSkeletonPlaceholder(
            Modifier
                .widthIn(max = coverWidth)
                .fillMaxWidth()
                .aspectRatio(coverWidth / coverHeight),
        )
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth().height(AppSpacing.Content))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.65f).height(AppSpacing.Item))
    }
}
