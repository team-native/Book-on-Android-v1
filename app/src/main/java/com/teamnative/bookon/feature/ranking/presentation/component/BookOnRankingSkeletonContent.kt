package com.teamnative.bookon.feature.ranking.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonLayout
import com.teamnative.bookon.core.ui.component.loading.BookOnSkeletonPlaceholder

@Composable
fun BookOnRankingSkeletonContent(modifier: Modifier = Modifier) {
    BookOnSkeletonLayout(modifier = modifier) {
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.6f).height(AppSpacing.Large))
        BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.8f).height(AppSpacing.Content))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
            verticalAlignment = Alignment.Bottom,
        ) {
            listOf(
                AppComponentSize.RankingSecondPedestalHeight,
                AppComponentSize.RankingFirstPedestalHeight,
                AppComponentSize.RankingThirdPedestalHeight,
            ).forEach { pedestalHeight ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
                ) {
                    BookOnSkeletonPlaceholder(
                        modifier = Modifier.size(AppIconSize.PodiumSecondaryAvatar),
                        shape = CircleShape,
                    )
                    BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(0.7f).height(AppSpacing.Content))
                    BookOnSkeletonPlaceholder(Modifier.fillMaxWidth().height(pedestalHeight))
                }
            }
        }
        repeat(5) {
            BookOnSkeletonPlaceholder(Modifier.fillMaxWidth(1f).height(AppComponentSize.RankingListRowHeight))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnRankingSkeletonContentPreview() {
    BookOnTheme {
        BookOnRankingSkeletonContent()
    }
}
