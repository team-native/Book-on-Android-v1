package com.teamnative.bookon.core.ui.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel

/**
 * 마이페이지의 대출 현황처럼 2~3개 수치를 한 줄 카드로 표시한다.
 * 값은 호출 측에서 이미 화면 문자열로 만든 뒤 전달한다.
 */
@Composable
fun BookOnStatSummaryCard(
    items: List<BookOnStatItemUiModel>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.StatSummaryCardHeight)
            .shadow(
                elevation = AppElevation.StrongCard,
                shape = RoundedCornerShape(AppRadius.Card),
            )
            .clip(RoundedCornerShape(AppRadius.Card))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(BookOnColor.Primary, BookOnColor.PrimaryDark),
                ),
            )
            .padding(vertical = AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = item.label,
                    style = BookOnTypography.caption,
                    color = BookOnColor.PrimaryLight,
                )

                Spacer(modifier = Modifier.height(AppSpacing.Tiny))

                Text(
                    text = item.value,
                    style = BookOnTypography.sectionTitle,
                    color = BookOnColor.PrimaryLight,
                )
            }

            if (index < items.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(AppStrokeWidth.Divider)
                        .fillMaxHeight()
                        .background(BookOnColor.PrimaryLight.copy(alpha = 0.45f)),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnStatSummaryCardPreview() {
    BookOnTheme {
        BookOnStatSummaryCard(
            items = listOf(
                BookOnStatItemUiModel("대출 중", "3권"),
                BookOnStatItemUiModel("반납 임박", "2권"),
                BookOnStatItemUiModel("누적 대출", "23권"),
            ),
        )
    }
}
