package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing

@Composable
fun BookOnBookDetailTopBar(onBackClick: () -> Unit) {
    val surface = MaterialTheme.colorScheme.surface
    val foreground = MaterialTheme.colorScheme.onSurface
    val darkFilter =
        if (surface.luminance() < 0.5f) {
            ColorFilter.colorMatrix(
                ColorMatrix(
                    floatArrayOf(
                        surface.red - foreground.red,
                        0f,
                        0f,
                        0f,
                        foreground.red * 255f,
                        0f,
                        surface.green - foreground.green,
                        0f,
                        0f,
                        foreground.green * 255f,
                        0f,
                        0f,
                        surface.blue - foreground.blue,
                        0f,
                        foreground.blue * 255f,
                        0f,
                        0f,
                        0f,
                        1f,
                        0f,
                    ),
                ),
            )
        } else {
            null
        }
    Box(
        modifier =
            Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(AppComponentSize.TopBarHeight)
                .padding(horizontal = AppSpacing.Tiny),
        contentAlignment = Alignment.CenterStart,
    ) {
        IconButton(onClick = onBackClick) {
            Image(
                painter = painterResource(R.drawable.book_detail_back),
                contentDescription = stringResource(R.string.book_detail_back),
                colorFilter = darkFilter,
                modifier =
                    Modifier.size(
                        width = AppComponentSize.BookDetailBackWidth,
                        height = AppComponentSize.BookDetailBackHeight,
                    ),
            )
        }
    }
}
