package com.teamnative.bookon.core.ui.component.book

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

/**
 * 서버의 도서 표지 URL을 비동기로 표시하고, URL 누락·로딩 실패 시 지정한 placeholder를 유지한다.
 */
@Composable
fun BookOnRemoteBookCover(
    coverImageUrl: String?,
    placeholderColor: Color,
    modifier: Modifier = Modifier,
) {
    val placeholderPainter = ColorPainter(placeholderColor)

    AsyncImage(
        model = coverImageUrl,
        contentDescription = null,
        placeholder = placeholderPainter,
        error = placeholderPainter,
        fallback = placeholderPainter,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}
