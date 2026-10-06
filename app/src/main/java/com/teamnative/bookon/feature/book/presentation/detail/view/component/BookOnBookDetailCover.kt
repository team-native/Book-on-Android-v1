package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.ui.component.book.BookOnRemoteBookCover

@Composable
fun BookOnBookDetailCover(
    coverImageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        BookOnRemoteBookCover(
            coverImageUrl = coverImageUrl,
            placeholderColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .widthIn(max = AppComponentSize.BookDetailCoverWidth)
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(AppElevation.BookCover)
                .testTag("book_detail_cover"),
        )
    }
}
