package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.AppAnimationDuration
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.feature.my.presentation.model.BookOnMyMarathonUiModel
import kotlinx.coroutines.delay

private val MyMarathonCardIconSize = 40.dp

/**
 * 독서마라톤 계정을 건너뛴 사용자를 위한 미연동 상태 카드이다.
 * 연동 카드와 동일한 구성을 유지하고 우측 참여 상태만 토글로 제공한다.
 */
@Composable
fun BookOnMyUnlinkedMarathonCard(
    uiState: BookOnMyMarathonUiModel,
    onLinkRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val toggleStateDescription = stringResource(R.string.reading_marathon_link_toggle_state_off)
    var isLinkSwitchChecked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isLinkSwitchChecked) {
        if (isLinkSwitchChecked) {
            delay(AppAnimationDuration.Medium.toLong())
            isLinkSwitchChecked = false
            onLinkRequest()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.Card))
            .background(BookOnColor.Surface)
            .border(AppStrokeWidth.Divider, BookOnColor.MarathonCardBorder, RoundedCornerShape(AppRadius.Card))
            .padding(AppSpacing.Content),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(MyMarathonCardIconSize),
                painter = painterResource(R.drawable.marathon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.width(AppSpacing.Content))

            Text(
                modifier = Modifier.weight(1f),
                text = uiState.title,
                style = BookOnTypography.topBarTitle,
                color = BookOnColor.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Switch(
                checked = isLinkSwitchChecked,
                onCheckedChange = { isChecked ->
                    if (isChecked) isLinkSwitchChecked = true
                },
                modifier = Modifier.semantics {
                    stateDescription = toggleStateDescription
                    testTag = "my_marathon_link_switch"
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = BookOnColor.Surface,
                    uncheckedTrackColor = BookOnColor.SwitchOff,
                    uncheckedBorderColor = BookOnColor.SwitchOff,
                ),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Content))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = uiState.progressText,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.width(AppSpacing.Small))

            Text(
                text = uiState.percentText,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.PrimaryPressed,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        MyMarathonProgressBar(
            progress = uiState.progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpacing.Item),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Content))

        Text(
            text = uiState.remainingText,
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** 기존 연동 카드와 같은 진행 바 스타일을 미연동 카드에도 적용한다. */
@Composable
private fun MyMarathonProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppRadius.Progress))
            .background(BookOnColor.SwitchOff),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(AppSpacing.Item)
                .clip(RoundedCornerShape(AppRadius.Progress))
                .background(BookOnColor.Primary),
        )
    }
}
