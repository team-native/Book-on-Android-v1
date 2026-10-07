package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

@Composable
fun BookOnBookDetailHeading(
    title: String,
    author: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
    ) {
        Text(
            text = title,
            style = bookOnTypography.bookDetailTitle,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = author,
            style = bookOnTypography.bookDetailAuthor,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
