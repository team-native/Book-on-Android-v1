package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation

@Composable
fun BookOnBookDetailCover(
    coverImageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .widthIn(max = AppComponentSize.BookDetailCoverWidth)
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .testTag("book_detail_cover"),
            contentAlignment = Alignment.Center,
        ) {
            val context = LocalContext.current
            val density = LocalDensity.current
            val slotWidth = maxWidth
            val slotHeight = maxHeight
            val slotWidthPx = with(density) { slotWidth.roundToPx() }
            val slotHeightPx = with(density) { slotHeight.roundToPx() }
            key(coverImageUrl, slotWidthPx, slotHeightPx) {
                val request = remember(context, coverImageUrl, slotWidthPx, slotHeightPx) {
                    ImageRequest.Builder(context)
                        .data(coverImageUrl)
                        .size(slotWidthPx.coerceAtLeast(1), slotHeightPx.coerceAtLeast(1))
                        .scale(Scale.FIT)
                        .build()
                }
                val painter = rememberAsyncImagePainter(request)
                val intrinsicSize = (painter.state as? AsyncImagePainter.State.Success)
                    ?.painter?.intrinsicSize
                val hasImageBounds = intrinsicSize != null &&
                    intrinsicSize.width.isFinite() && intrinsicSize.height.isFinite() &&
                    intrinsicSize.width > 0f && intrinsicSize.height > 0f
                val imageModifier = if (hasImageBounds) {
                    val imageRatio = intrinsicSize.width / intrinsicSize.height
                    val fittedWidth = minOf(slotWidth, slotHeight * imageRatio)
                    Modifier
                        .size(fittedWidth, fittedWidth / imageRatio)
                        .shadow(AppElevation.BookCover)
                        .testTag("book_detail_cover_image")
                } else {
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .testTag("book_detail_cover_placeholder")
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = imageModifier,
                )
            }
        }
    }
}
