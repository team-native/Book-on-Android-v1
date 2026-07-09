package com.teamnative.bookon.ui.Component.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.teamnative.bookon.theme.AppComponentSize
import com.teamnative.bookon.theme.AppElevation
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography

private val DetailCoverHeight = 236.dp

/**
 * 상세 화면의 큰 책 표지 영역이다.
 * 실제 표지는 서버 이미지 로더가 준비되면 cover slot으로 주입한다.
 */
@Composable
fun BookOnBookDetailCover(
    modifier: Modifier = Modifier,
    cover: @Composable () -> Unit = { BookDetailCoverPlaceholder() },
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
        cover()
    }
}

@Composable
private fun BookDetailCoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BookOnColor.BookCoverPlaceholder),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailCoverPreview() {
    BookOnTheme {
        Row(modifier = Modifier.padding(AppSpacing.ScreenHorizontal)) {
            BookOnBookDetailCover(
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
