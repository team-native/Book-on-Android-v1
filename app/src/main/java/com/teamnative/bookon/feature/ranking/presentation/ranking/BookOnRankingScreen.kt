package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.feature.ranking.presentation.component.BookOnRankingListCard
import com.teamnative.bookon.feature.ranking.presentation.component.BookOnRankingPodium
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.model.resolve

/**
 * 랭킹 화면은 포디움과 4위 이후 목록을 기존 랭킹 컴포넌트로 조립한다.
 */
@Composable
fun BookOnRankingScreen(
    uiState: BookOnRankingScreenUiState,
    bottomBar: @Composable () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            uiState.errorMessage?.let { message ->
                item {
                    Text(
                        text = message.resolve(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Button(onClick = onRetryClick) {
                        Text(text = stringResource(R.string.action_retry))
                    }
                }
            }
            if (uiState.errorMessage == null && uiState.list.members.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.empty_ranking),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (uiState.errorMessage == null && uiState.list.members.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Tiny)) {
                        Text(
                            text = stringResource(R.string.ranking_title),
                            style = bookOnTypography.screenTitle,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = uiState.description,
                            style = bookOnTypography.caption,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                item {
                    BookOnRankingPodium(uiState = uiState.podium)
                }
                item {
                    BookOnRankingListCard(uiState = uiState.list)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnRankingScreenPreview() {
    BookOnTheme {
        BookOnRankingScreen(
            uiState = sampleRankingUiState(),
            bottomBar = {},
            onRetryClick = {},
        )
    }
}
