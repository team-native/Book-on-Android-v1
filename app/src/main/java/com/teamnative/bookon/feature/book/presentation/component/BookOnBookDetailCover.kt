package com.teamnative.bookon.feature.book.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.book.BookOnRemoteBookCover

private val DetailCoverHeight = 236.dp

/**
 * 상세 화면의 큰 책 표지 영역이다.
 * 서버 표지 URL을 표시하고, URL 누락·로딩 실패 시 기존 placeholder를 유지한다.
 */
@Composable
fun BookOnBookDetailCover(
    coverImageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(DetailCoverHeight)
            .shadow(
                elevation = AppElevation.BookCover,
                shape = RoundedCornerShape(AppRadius.Card),
            )
            .clip(RoundedCornerShape(AppRadius.Card)),
        contentAlignment = Alignment.Center,
    ) {
        BookOnRemoteBookCover(
            coverImageUrl = coverImageUrl,
            placeholderColor = BookOnColor.BookCoverPlaceholder,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailCoverPreview() {
    BookOnTheme {
        Row(modifier = Modifier.padding(AppSpacing.ScreenHorizontal)) {
            BookOnBookDetailCover(
                coverImageUrl = null,
                modifier = Modifier.size(
                    width = AppComponentSize.BookCoverWidth,
                    height = DetailCoverHeight,
                ),
            )
            Text(
                text = "",
                style = BookOnTypography.bodyMedium,
            )
        }
    }
}
