package com.example.bookon.ui.commonComponent.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.ui.commonComponent.row.BookOnSwitchRow
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.BookOnSwitchRowUiState
import com.example.bookon.uiState.my.BookOnNotificationSettingsUiState

/**
 * 알림 설정에서 사용하는 스위치 목록 패널이다.
 * 각 행의 실제 저장은 호출 화면의 onCheckedChange에서 처리한다.
 */
@Composable
fun BookOnNotificationSettingsPanel(
    uiState: BookOnNotificationSettingsUiState,
    onCheckedChange: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = AppRadius.LargeCard, topEnd = AppRadius.LargeCard))
            .background(BookOnColor.Surface)
            .padding(AppSpacing.Content),
    ) {
        Text(
            text = uiState.title,
            style = BookOnTypography.sectionTitle,
            color = BookOnColor.TextPrimary,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Tiny))
        Text(
            text = uiState.description,
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Content))
        uiState.rows.forEachIndexed { index, row ->
            BookOnSwitchRow(
                uiState = row,
                onCheckedChange = { checked -> onCheckedChange(index, checked) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnNotificationSettingsPanelPreview() {
    BookOnTheme {
        BookOnNotificationSettingsPanel(
            uiState = BookOnNotificationSettingsUiState(
                title = "알림 설정",
                description = "받고 싶은 알림을 선택하세요",
                rows = listOf(
                    BookOnSwitchRowUiState("반납 알림", true, "반납 3일 전과 당일에 알려드려요"),
                ),
            ),
            onCheckedChange = { _, _ -> },
        )
    }
}
