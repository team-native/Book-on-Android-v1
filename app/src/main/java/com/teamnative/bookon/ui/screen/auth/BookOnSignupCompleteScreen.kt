package com.teamnative.bookon.ui.screen.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.ui.Component.auth.BookOnSignupCompleteSummary
import com.teamnative.bookon.ui.Component.button.BookOnPrimaryButton
import com.teamnative.bookon.uiState.auth.BookOnSignupCompleteUiState

/**
 * 가입 완료 화면은 서버 사용자 요약이 들어올 위치를 샘플 상태로 표시한다.
 */
@Composable
fun BookOnSignupCompleteScreen(
    uiState: BookOnSignupCompleteUiState,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.AuthHorizontal)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(LoginTitleTopSpacing))
            Text(
                text = uiState.title,
                style = BookOnTypography.screenTitle,
                color = BookOnColor.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Content))
            Text(
                text = uiState.message,
                style = BookOnTypography.bodyMedium,
                color = BookOnColor.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
            BookOnSignupCompleteSummary(
                ownedBookCountText = uiState.ownedBookCountText,
                ownedBookDescription = stringResource(R.string.owned_books),
                marathonStatusText = uiState.marathonStatusText,
                marathonDescription = stringResource(R.string.reading_marathon),
            )
            Spacer(modifier = Modifier.weight(1f))
            BookOnPrimaryButton(
                modifier = Modifier.padding(horizontal = AppSpacing.Small),
                text = stringResource(R.string.action_start),
                onClick = onStartClick,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
        }
    }
}
