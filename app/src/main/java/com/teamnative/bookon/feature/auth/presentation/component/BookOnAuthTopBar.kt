package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar

@Composable
internal fun BookOnAuthTopBar(
    onBackClick: () -> Unit,
    onPasswordNoticeClick: () -> Unit,
) {
    BookOnTopBar(
        title = "",
        onBackClick = onBackClick,
        trailingContent = {
            Row(
                modifier = Modifier.clickable(role = Role.Button, onClick = onPasswordNoticeClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.password_warning_action),
                    style = BookOnTypography.caption.copy(fontWeight = FontWeight.Normal),
                    color = BookOnColor.TextTertiary,
                )
                Spacer(modifier = Modifier.width(AppSpacing.Tiny))
                Text(
                    text = stringResource(R.string.info_mark),
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextTertiary,
                )
            }
        },
    )
}
