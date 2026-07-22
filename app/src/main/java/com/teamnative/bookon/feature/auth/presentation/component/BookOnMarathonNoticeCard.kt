package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppAnimationDuration
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

private val MarathonNoticeHeight = 55.dp

@Composable
internal fun BookOnMarathonNoticeCard(
    text: String,
    isLinked: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isLinked) BookOnColor.PrimaryContainer else BookOnColor.Surface,
        animationSpec = tween(AppAnimationDuration.Medium),
        label = "marathonNoticeBackgroundColor",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MarathonNoticeHeight)
            .shadow(AppElevation.StrongCard, RoundedCornerShape(AppRadius.Field))
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(backgroundColor)
            .padding(horizontal = AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.info_mark),
            style = BookOnTypography.caption,
            color = if (isLinked) BookOnColor.PrimaryPressed else BookOnColor.TextTertiary,
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = text,
            transitionSpec = {
                fadeIn(animationSpec = tween(AppAnimationDuration.Medium)) togetherWith
                    fadeOut(animationSpec = tween(AppAnimationDuration.Medium))
            },
            label = "marathonNoticeText",
        ) { noticeText ->
            Text(
                text = noticeText,
                style = BookOnTypography.caption,
                color = if (isLinked) BookOnColor.TextDarkGray else BookOnColor.TextSecondary,
            )
        }
    }
}
