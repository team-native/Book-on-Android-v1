package com.teamnative.bookon.feature.book.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.ui.component.textfield.BookOnTextField
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme

/**
 * 검색 화면 상단의 검색 입력 바이다.
 * 서버 검색은 호출 측 query 상태 변화와 검색 액션에 연결한다.
 */
@Composable
fun BookOnSearchBar(
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        leadingIcon = { SearchIcon() },
    )
}

@Composable
private fun SearchIcon() {
    Canvas(modifier = Modifier.size(AppIconSize.Small)) {
        val strokeWidth = 1.8.dp.toPx()
        drawCircle(
            color = BookOnColor.TextPlaceholder,
            radius = size.minDimension * 0.32f,
            center = Offset(size.width * 0.43f, size.height * 0.43f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
            ),
        )
        drawLine(
            color = BookOnColor.TextPlaceholder,
            start = Offset(size.width * 0.66f, size.height * 0.66f),
            end = Offset(size.width * 0.84f, size.height * 0.84f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSearchBarPreview() {
    BookOnTheme {
        BookOnSearchBar(
            query = "클린",
            placeholder = "검색",
            onQueryChange = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
