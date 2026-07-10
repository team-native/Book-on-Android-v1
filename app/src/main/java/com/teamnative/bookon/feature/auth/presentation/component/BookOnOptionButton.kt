package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.feature.auth.presentation.model.BookOnOptionButtonUiModel

/**
 * 성별처럼 두 개 이상의 선택지 중 하나를 고르는 버튼이다.
 * selected 상태는 selectable semantics에 연결한다.
 */
@Composable
fun BookOnOptionButton(
    uiState: BookOnOptionButtonUiModel,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnOptionButton(
        text = uiState.text,
        selected = uiState.selected,
        onSelected = onSelected,
        modifier = modifier,
        enabled = uiState.enabled,
    )
}

/**
 * 성별처럼 두 개 이상의 선택지 중 하나를 고르는 버튼이다.
 * selected 상태는 selectable semantics에 연결한다.
 */
@Composable
fun BookOnOptionButton(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .height(AppComponentSize.ButtonHeight)
            .shadow(
                elevation = AppElevation.Button,
                shape = RoundedCornerShape(AppRadius.Chip),
            )
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(if (selected) BookOnColor.PrimaryContainer else BookOnColor.Surface)
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onSelected,
            )
            .alpha(if (enabled) 1f else 0.55f),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = BookOnTypography.bodySemiBold,
            color = if (selected) BookOnColor.PrimaryPressed else BookOnColor.TextPlaceholder,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnOptionButtonPreview() {
    BookOnTheme {
        Row(modifier = Modifier.padding(AppSpacing.ScreenHorizontal)) {
            BookOnOptionButton(
                text = "남자",
                selected = true,
                onSelected = {},
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(AppSpacing.Content))
            BookOnOptionButton(
                text = "여자",
                selected = false,
                onSelected = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
