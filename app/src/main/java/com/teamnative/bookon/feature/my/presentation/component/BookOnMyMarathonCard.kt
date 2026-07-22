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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.feature.my.presentation.model.BookOnMyMarathonUiModel

private val MyMarathonCardIconSize = 40.dp
private val MyMarathonCardBadgeHorizontalPadding = 12.dp
private val MyMarathonCardBadgeVerticalPadding = 8.dp

/**
 * 독서마라톤 계정이 연동된 사용자의 진행 상태 카드이다.
 */
@Composable
fun BookOnMyMarathonCard(
    uiState: BookOnMyMarathonUiModel,
    modifier: Modifier = Modifier,
) {
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
            MyMarathonStatusBadge(
                text = uiState.statusText,
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

@Composable
private fun MyMarathonStatusBadge(
    text: String,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadius.Small))
            .background(BookOnColor.StatusAvailableContainer)
            .padding(
                horizontal = MyMarathonCardBadgeHorizontalPadding,
                vertical = MyMarathonCardBadgeVerticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = BookOnTypography.badge,
            color = BookOnColor.PrimaryPressed,
            maxLines = 1,
        )
    }
}
