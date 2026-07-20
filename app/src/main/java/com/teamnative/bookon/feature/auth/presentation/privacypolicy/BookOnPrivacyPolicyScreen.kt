package com.teamnative.bookon.feature.auth.presentation.privacypolicy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.component.bar.BookOnStepProgress
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.card.BookOnInfoCard

/**
 * 개인정보 안내 화면은 긴 약관 내용을 스크롤 가능한 카드 목록으로 표시한다.
 */
@Composable
fun BookOnPrivacyPolicyScreen(
    uiState: BookOnPrivacyPolicyUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = uiState.title,
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.AuthHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.AuthHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item { BookOnStepProgress(currentStep = 2, totalStep = 3) }
            uiState.sections.forEach { section ->
                item {
                    BookOnInfoCard(
                        title = section.title,
                        description = section.body,
                    )
                }
            }
            item {
                Text(
                    text = uiState.notice,
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextSecondary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPrivacyPolicyScreenPreview() {
    BookOnTheme {
        BookOnPrivacyPolicyScreen(uiState = samplePrivacyPolicyUiState(), onBackClick = {})
    }
}
