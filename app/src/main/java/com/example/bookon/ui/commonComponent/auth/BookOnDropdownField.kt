package com.example.bookon.ui.commonComponent.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookon.R
import com.example.bookon.theme.AppComponentSize
import com.example.bookon.theme.AppElevation
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.auth.BookOnDropdownFieldUiState

/**
 * 학과 선택처럼 클릭하면 외부 메뉴를 여는 드롭다운 형태 필드이다.
 * 실제 메뉴 표시와 선택 상태 변경은 호출 측에서 처리한다.
 */
@Composable
fun BookOnDropdownField(
    uiState: BookOnDropdownFieldUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnDropdownField(
        text = uiState.text,
        placeholder = uiState.placeholder,
        onClick = onClick,
        modifier = modifier,
        expanded = uiState.expanded,
        enabled = uiState.enabled,
    )
}

/**
 * 학과 선택처럼 클릭하면 외부 메뉴를 여는 드롭다운 형태 필드이다.
 * 실제 메뉴 표시와 선택 상태 변경은 호출 측에서 처리한다.
 */
@Composable
fun BookOnDropdownField(
    text: String,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.FieldHeight)
            .shadow(
                elevation = AppElevation.Field,
                shape = RoundedCornerShape(AppRadius.Field),
            )
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(BookOnColor.Surface)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .alpha(if (enabled) 1f else 0.55f)
            .padding(horizontal = AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text.ifEmpty { placeholder },
            style = BookOnTypography.fieldText,
            color = if (text.isEmpty()) BookOnColor.TextPlaceholder else BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Item))
        Image(
            modifier = Modifier
                .size(width = 13.dp, height = 6.dp),
            painter = painterResource(if (expanded) R.drawable.up_arrow else R.drawable.down_arrow),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnDropdownFieldPreview() {
    BookOnTheme {
        BookOnDropdownField(
            text = "",
            placeholder = "학과",
            onClick = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
