package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

@Composable
fun BookOnBookDetailIntroduction(
    intro: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
    ) {
        Text(
            text = stringResource(R.string.book_intro),
            style = bookOnTypography.bookDetailInfoValue,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = intro.ifBlank { stringResource(R.string.book_detail_no_intro) },
            style = bookOnTypography.bookDetailIntro,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
