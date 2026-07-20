package com.teamnative.bookon.feature.ranking.presentation.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.feature.ranking.presentation.component.BookOnRankingListCard
import com.teamnative.bookon.feature.ranking.presentation.component.BookOnRankingPodium
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

/**
 * 랭킹 화면은 포디움과 4위 이후 목록을 기존 랭킹 컴포넌트로 조립한다.
 */
@Composable
fun BookOnRankingScreen(
    uiState: BookOnRankingScreenUiState,
    bottomBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = BookOnColor.Background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Tiny)) {
                    Text(
                        text = stringResource(R.string.ranking_title),
                        style = BookOnTypography.screenTitle,
                        color = BookOnColor.TextPrimary,
                    )
                    Text(
                        text = uiState.description,
                        style = BookOnTypography.caption,
                        color = BookOnColor.TextSecondary,
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

@Preview(showBackground = true)
@Composable
private fun BookOnRankingScreenPreview() {
    BookOnTheme {
        BookOnRankingScreen(
            uiState = sampleRankingUiState(),
            bottomBar = {},
        )
    }
}
