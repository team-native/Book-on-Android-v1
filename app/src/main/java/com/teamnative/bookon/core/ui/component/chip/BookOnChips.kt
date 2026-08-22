package com.teamnative.bookon.core.ui.component.chip

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

/**
 * 검색 필터와 내역 상태 필터에 쓰는 선택형 칩이다.
 * 선택 상태는 배경색과 텍스트 색상만으로 구분되지 않도록 selected 값을 semantics에 연결한다.
 */
@Composable
fun BookOnFilterChip(
    uiState: BookOnFilterChipUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnFilterChip(
        text = uiState.text,
        selected = uiState.selected,
        onClick = onClick,
        modifier = modifier,
    )
}

/**
 * 검색 필터와 내역 상태 필터에 쓰는 선택형 칩이다.
 * 선택 상태는 배경색과 텍스트 색상만으로 구분되지 않도록 selected 값을 semantics에 연결한다.
 */
@Composable
fun BookOnFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppComponentSize.ChipHeight)
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = AppSpacing.Content),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = bookOnTypography.chip,
            color = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnFilterChipPreview() {
    BookOnTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
            BookOnFilterChip(text = "대출 중 2", selected = false, onClick = {})
            BookOnFilterChip(text = "반납 완료", selected = true, onClick = {})
        }
    }
}
