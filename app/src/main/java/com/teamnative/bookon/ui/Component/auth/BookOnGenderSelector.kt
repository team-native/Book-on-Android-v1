package com.teamnative.bookon.ui.Component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.auth.BookOnOptionButtonUiState

/**
 * 성별 선택 입력 섹션을 라벨과 옵션 Row로 표시한다.
 * 선택 이벤트는 옵션 index로 호출 화면에 전달한다.
 */
@Composable
fun BookOnGenderSelector(
    options: List<BookOnOptionButtonUiState>,
    onGenderSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
    ) {
        Text(
            text = stringResource(R.string.gender),
            style = BookOnTypography.fieldLabel,
            color = BookOnColor.TextPrimary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content)) {
            options.forEachIndexed { index, option ->
                BookOnOptionButton(
                    uiState = option,
                    onSelected = { onGenderSelected(index) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
