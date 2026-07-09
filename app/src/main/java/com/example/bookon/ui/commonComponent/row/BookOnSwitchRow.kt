package com.example.bookon.ui.commonComponent.row

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.BookOnSwitchRowUiState

/**
 * 제목과 설명, Switch를 한 행으로 묶어 알림/연동 설정에 사용한다.
 */
@Composable
fun BookOnSwitchRow(
    uiState: BookOnSwitchRowUiState,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnSwitchRow(
        title = uiState.title,
        checked = uiState.checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        description = uiState.description,
    )
}

/**
 * 제목과 설명, Switch를 한 행으로 묶어 알림/연동 설정에 사용한다.
 */
@Composable
fun BookOnSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.Item, horizontal = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextPrimary,
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(AppSpacing.Tiny))
                Text(
                    text = description,
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextSecondary,
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BookOnColor.Surface,
                checkedTrackColor = BookOnColor.Primary,
                uncheckedThumbColor = BookOnColor.Surface,
                uncheckedTrackColor = BookOnColor.SwitchOff,
                uncheckedBorderColor = BookOnColor.SwitchOff,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSwitchRowPreview() {
    BookOnTheme {
        BookOnSwitchRow(
            title = "반납 알림",
            description = "반납 3일 전과 당일에 알려드려요",
            checked = true,
            onCheckedChange = {},
        )
    }
}
