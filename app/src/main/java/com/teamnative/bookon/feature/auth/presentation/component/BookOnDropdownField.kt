package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.feature.auth.presentation.model.BookOnDropdownFieldUiModel

private val DropdownIndicatorWidth = 13.dp
private val DropdownIndicatorHeight = 6.dp

/**
 * 학과 선택처럼 클릭하면 외부 메뉴를 여는 드롭다운 형태 필드이다.
 * 실제 메뉴 표시와 선택 상태 변경은 호출 측에서 처리한다.
 */
@Composable
fun BookOnDropdownField(
    uiState: BookOnDropdownFieldUiModel,
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
            .background(MaterialTheme.colorScheme.surface)
            .alpha(if (enabled) 1f else 0.55f)
            .padding(horizontal = AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text.ifEmpty { placeholder },
            style = bookOnTypography.fieldText,
            color = if (text.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier.size(AppComponentSize.MinTouchTarget),
            enabled = enabled,
            onClick = onClick,
        ) {
            Image(
                modifier = Modifier.size(
                    width = DropdownIndicatorWidth,
                    height = DropdownIndicatorHeight,
                ),
                painter = painterResource(if (expanded) R.drawable.up_arrow else R.drawable.down_arrow),
                contentDescription = stringResource(
                    if (expanded) {
                        R.string.department_dropdown_collapse_description
                    } else {
                        R.string.department_dropdown_expand_description
                    },
                ),
                contentScale = ContentScale.Fit,
            )
        }
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
