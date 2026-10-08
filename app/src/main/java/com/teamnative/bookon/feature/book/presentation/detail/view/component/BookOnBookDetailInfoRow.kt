package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.model.BookOnBookDetailInfoItemUiModel
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.model.BookOnBookDetailInfoRowUiModel

private const val DetailInfoShadowOpacity = 0.25f

@Composable
fun BookOnBookDetailInfoRow(
    uiState: BookOnBookDetailInfoRowUiModel,
    modifier: Modifier = Modifier,
) {
    BookOnBookDetailInfoRow(items = uiState.items, modifier = modifier)
}

@Composable
fun BookOnBookDetailInfoRow(
    items: List<BookOnBookDetailInfoItemUiModel>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) {
        return
    }
    val fontScale = LocalDensity.current.fontScale
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val tileWidth = AppComponentSize.BookDetailInfoMinWidth * fontScale
        val columns =
            when {
                maxWidth >= tileWidth * 3 + AppSpacing.Item * 2 -> 3
                maxWidth >= tileWidth * 2 + AppSpacing.Item -> 2
                else -> 1
            }
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
            items.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
                ) {
                    rowItems.forEach { infoItem ->
                        BookOnBookDetailInfoItem(
                            infoItem = infoItem,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookOnBookDetailInfoItem(
    infoItem: BookOnBookDetailInfoItemUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .heightIn(min = AppComponentSize.BookDetailInfoMinHeight)
                .shadow(
                    elevation = AppElevation.Card,
                    shape = RoundedCornerShape(AppRadius.Chip),
                    ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = DetailInfoShadowOpacity),
                    spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = DetailInfoShadowOpacity),
                ).background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(AppRadius.Chip),
                ).padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Item),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small, Alignment.CenterVertically),
    ) {
        Text(
            text = infoItem.label,
            style = bookOnTypography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Text(
            text = infoItem.value,
            style = bookOnTypography.bookDetailInfoValue,
            color =
                if (infoItem.highlighted) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            textAlign = TextAlign.Center,
        )
    }
}
