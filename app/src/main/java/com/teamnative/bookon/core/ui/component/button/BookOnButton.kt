package com.teamnative.bookon.core.ui.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
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
) {
    val isClickable = enabled && !loading
    val targetBackgroundColor = if (enabled) BookOnColor.Primary else BookOnColor.Disabled
    val targetContentColor = if (enabled) BookOnColor.Surface else BookOnColor.TextPlaceholder
    val backgroundColor by animateColorAsState(targetValue = targetBackgroundColor)
    val contentColor by animateColorAsState(targetValue = targetContentColor)
    val loadingDescription = stringResource(R.string.state_loading)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.ButtonHeight)
            .shadow(
                elevation = if (enabled) AppElevation.Button else AppElevation.None,
                shape = RoundedCornerShape(AppRadius.Button),
            )
            .clip(RoundedCornerShape(AppRadius.Button))
            .background(backgroundColor)
            .semantics {
                if (!enabled) disabled()
                if (loading) stateDescription = loadingDescription
            }
            .clickable(
                enabled = isClickable,
                role = Role.Button,
                onClick = onClick,
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
                style = BookOnTypography.button,
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
