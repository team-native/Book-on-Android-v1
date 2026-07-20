package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.core.ui.component.row.BookOnSwitchRow

/** 알림 종류 선택을 위해 ModalBottomSheet 안에 표시하는 독립 콘텐츠다. */
@Composable
fun BookOnNotificationSettingsBottomSheetContent(
    notificationSelections: List<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val notificationRows = listOf(
        NotificationSettingRow(
            title = stringResource(R.string.return_notification),
            description = stringResource(R.string.return_notification_description),
        ),
        NotificationSettingRow(
            title = stringResource(R.string.library_notice_notification),
            description = stringResource(R.string.library_notice_notification_description),
        ),
        NotificationSettingRow(
            title = stringResource(R.string.library_notice_notification),
            description = stringResource(R.string.library_notice_notification_description),
        ),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = AppSpacing.ScreenHorizontal)
            .padding(bottom = AppSpacing.Section),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppSpacing.Item)
                .height(AppSpacing.Tiny),
        ) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = AppComponentSize.BottomSheetHandleWidth, height = AppSpacing.Tiny)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(BookOnColor.Disabled),
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.Large))
        Text(
            text = stringResource(R.string.notification_settings),
            style = BookOnTypography.sectionTitle.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
            ),
            color = BookOnColor.TextDarkGray,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        Text(
            text = stringResource(R.string.notification_select_title),
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
        )
        Spacer(modifier = Modifier.height(AppSpacing.XLarge))

        notificationRows.forEachIndexed { index, row ->
            BookOnSwitchRow(
                title = row.title,
                description = row.description,
                checked = notificationSelections.getOrElse(index) { false },
                onCheckedChange = { checked -> onCheckedChange(index, checked) },
                modifier = Modifier.semantics { testTag = "notification_setting_row_$index" },
                switchModifier = Modifier.semantics { testTag = "notification_switch_$index" },
            )
            HorizontalDivider(
                color = BookOnColor.Divider,
                thickness = AppStrokeWidth.Divider,
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.Section + AppSpacing.Content))
        BookOnPrimaryButton(
            text = stringResource(R.string.action_complete_signup),
            onClick = onCompleteClick,
            modifier = Modifier.semantics { testTag = "notification_complete_button" },
        )
    }
}

private data class NotificationSettingRow(
    val title: String,
    val description: String,
)

@Preview(showBackground = true)
@Composable
private fun BookOnNotificationSettingsBottomSheetContentPreview() {
    BookOnTheme {
        BookOnNotificationSettingsBottomSheetContent(
            notificationSelections = listOf(true, true, false),
            onCheckedChange = { _, _ -> },
            onCompleteClick = {},
        )
    }
}
