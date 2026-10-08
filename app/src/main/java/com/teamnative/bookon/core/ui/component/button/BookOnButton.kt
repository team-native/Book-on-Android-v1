package com.teamnative.bookon.core.ui.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

/**
 * Figma의 52dp 녹색 CTA 버튼을 앱 공통 스타일로 제공한다.
 * enabled와 loading 상태에 따라 클릭 가능 여부와 표시 방식을 함께 제어한다.
 */
@Composable
fun BookOnPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(AppRadius.Button),
    textStyle: TextStyle = bookOnTypography.button,
    fixedHeight: Dp? = AppComponentSize.ButtonHeight,
) {
    val isClickable = enabled && !loading
    val targetBackgroundColor = if (enabled) containerColor else MaterialTheme.colorScheme.surfaceVariant
    val targetContentColor =
        if (enabled) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    val backgroundColor by animateColorAsState(targetValue = targetBackgroundColor)
    val contentColor by animateColorAsState(targetValue = targetContentColor)
    val loadingDescription = stringResource(R.string.state_loading)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (fixedHeight != null) {
                        Modifier.height(fixedHeight)
                    } else {
                        Modifier.heightIn(min = AppComponentSize.ButtonHeight)
                    },
                ).shadow(
                    elevation = if (enabled) AppElevation.Button else AppElevation.None,
                    shape = shape,
                ).clip(shape)
                .background(backgroundColor)
                .semantics {
                    if (!enabled) disabled()
                    if (loading) stateDescription = loadingDescription
                }.clickable(
                    enabled = isClickable,
                    role = Role.Button,
                    onClick = onClick,
                ).then(
                    if (fixedHeight == null) {
                        Modifier.padding(vertical = AppSpacing.Medium)
                    } else {
                        Modifier
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(AppComponentSize.ChipHeight / 2),
                color = contentColor,
                strokeWidth = AppRadius.Progress,
            )
        } else {
            Text(
                text = text,
                style = textStyle,
                color = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPrimaryButtonPreview() {
    BookOnTheme {
        BookOnPrimaryButton(
            text = "다음",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPrimaryButtonDisabledPreview() {
    BookOnTheme {
        BookOnPrimaryButton(
            text = "다음",
            onClick = {},
            enabled = false,
        )
    }
}
