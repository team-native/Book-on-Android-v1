package com.teamnative.bookon.ui.Component.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.uiState.book.BookOnBookDetailInfoItemUiState
import com.teamnative.bookon.uiState.book.BookOnBookDetailInfoRowUiState

/**
 * 상세 화면의 도서관 번호, 재고 수량, 대출 가능 여부 같은 정보를 같은 너비 타일로 표시한다.
 * items가 비어 있으면 아무것도 그리지 않는다.
 */
@Composable
fun BookOnBookDetailInfoRow(
    uiState: BookOnBookDetailInfoRowUiState,
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
    items: List<BookOnBookDetailInfoItemUiState>,
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

@Preview(showBackground = true)
@Composable
private fun BookOnBookDetailInfoRowPreview() {
    BookOnTheme {
        BookOnBookDetailInfoRow(
            items = listOf(
                BookOnBookDetailInfoItemUiState("도서관 번호", "000"),
                BookOnBookDetailInfoItemUiState("재고 수량", "2권"),
                BookOnBookDetailInfoItemUiState("대출 여부", "가능", highlighted = true),
            ),
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
