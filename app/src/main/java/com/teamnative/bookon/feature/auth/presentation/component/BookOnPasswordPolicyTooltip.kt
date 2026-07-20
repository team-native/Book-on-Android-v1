package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import kotlinx.coroutines.launch

/** 모든 비밀번호 입력 화면에서 같은 규칙을 안내하는 접근 가능한 툴팁 트리거다. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookOnPasswordPolicyTooltip(modifier: Modifier = Modifier) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val coroutineScope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Below,
        ),
        tooltip = {
            RichTooltip(
                title = {
                    Text(
                        text = stringResource(R.string.password_precaution),
                        style = BookOnTypography.topBarTitle,
                    )
                },
                caretShape = null,
                maxWidth = 240.dp,
                shape = RoundedCornerShape(AppRadius.LargeCard),
                colors = TooltipDefaults.richTooltipColors(
                    containerColor = BookOnColor.Surface,
                    contentColor = BookOnColor.TextPrimary,
                    titleContentColor = BookOnColor.TextPrimary,
                ),
                shadowElevation = AppElevation.Field,
            ) {
                Text(text = stringResource(R.string.password_rule), style = BookOnTypography.bodyMedium)
            }
        },
        state = tooltipState,
        onDismissRequest = tooltipState::dismiss,
        enableUserInput = false,
    ) {
        Row(
            modifier = modifier
                .heightIn(min = AppComponentSize.MinTouchTarget)
                .clickable(
                    role = Role.Button,
                    onClick = {
                        if (tooltipState.isVisible) {
                            tooltipState.dismiss()
                        } else {
                            coroutineScope.launch { tooltipState.show() }
                        }
                    },
                ),
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
    }
}
