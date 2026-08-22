package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

private val MarathonCardHeight = 92.dp

@Composable
internal fun BookOnMarathonToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MarathonCardHeight)
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(MaterialTheme.colorScheme.background)
            .border(AppStrokeWidth.SelectedBorder, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(AppRadius.Field))
            .padding(AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppIconSize.XLarge)
                .clip(RoundedCornerShape(AppRadius.IconButton))
                .background(MaterialTheme.colorScheme.surface)
                .padding(6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.marathon),
                contentDescription = stringResource(R.string.reading_marathon_logo_description),
                contentScale = ContentScale.Fit,
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Content))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = bookOnTypography.bodySemiBold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(AppSpacing.Tiny))

            Text(text = description, style = bookOnTypography.caption, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
