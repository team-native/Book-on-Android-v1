package com.teamnative.bookon.feature.home.presentation.notices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar

@Composable
fun BookOnNoticeSummaryScreen(
    title: String,
    createdAt: String,
    summary: String,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.notices_summary),
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(createdAt, style = MaterialTheme.typography.labelMedium)
            Text(summary, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
