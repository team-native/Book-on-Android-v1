package com.teamnative.bookon.feature.book.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoItemUiModel
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoRowUiModel

private val BookDetailInfoItemHeight = 68.dp

/**
 * 상세 화면의 도서관 번호, 재고 수량, 대출 가능 여부 같은 정보를 같은 너비 타일로 표시한다.
 * items가 비어 있으면 아무것도 그리지 않는다.
 */
@Composable
fun BookOnBookDetailInfoRow(
    uiState: BookOnBookDetailInfoRowUiModel,
    modifier: Modifier = Modifier,
) {
    BookOnBookDetailInfoRow(
        items = uiState.items,
        modifier = modifier,
    )
}

/**
 * 상세 화면의 도서관 번호, 재고 수량, 대출 가능 여부 같은 정보를 같은 너비 타일로 표시한다.
 * items가 비어 있으면 아무것도 그리지 않는다.
 */
@Composable
fun BookOnBookDetailInfoRow(
    items: List<BookOnBookDetailInfoItemUiModel>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) {
        return
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        items.forEach { item ->
            BookOnBookDetailInfoItem(
                item = item,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** 상세 정보 행에서만 사용하는 단일 정보 타일이다. */
@Composable
private fun BookOnBookDetailInfoItem(
    item: BookOnBookDetailInfoItemUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(BookDetailInfoItemHeight)
            .clip(RoundedCornerShape(AppRadius.Small))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Item),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = item.label,
            style = bookOnTypography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Tiny))

        Text(
            text = item.value,
            style = bookOnTypography.bodySemiBold,
            color = if (item.highlighted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailInfoRowPreview() {
    BookOnTheme {
        BookOnBookDetailInfoRow(
            items = listOf(
                BookOnBookDetailInfoItemUiModel("도서관 번호", "000"),
                BookOnBookDetailInfoItemUiModel("재고 수량", "2권"),
                BookOnBookDetailInfoItemUiModel("대출 여부", "가능", highlighted = true),
            ),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
