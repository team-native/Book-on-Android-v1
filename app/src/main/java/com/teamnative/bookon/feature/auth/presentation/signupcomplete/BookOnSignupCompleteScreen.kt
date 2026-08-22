package com.teamnative.bookon.feature.auth.presentation.signupcomplete

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupCompleteCheckIcon
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupCompleteHeroBackground
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupCompleteSummary
import com.teamnative.bookon.feature.auth.presentation.component.SignupCompleteBottomSpacing
import com.teamnative.bookon.feature.auth.presentation.component.SignupCompleteContentTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.SignupCompleteSummarySpacing
import com.teamnative.bookon.feature.auth.presentation.component.SignupCompleteTitleSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme

/**
 * 가입 완료 화면은 Figma 시안에 맞춰 완료 상태와 사용자 요약을 표시한다.
 */
@Composable
fun BookOnSignupCompleteScreen(
    uiState: BookOnSignupCompleteUiState,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            BookOnSignupCompleteHeroBackground()
            Column(
                modifier = Modifier
                    .padding(horizontal = AppSpacing.AuthHorizontal)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.height(SignupCompleteContentTopSpacing))

                BookOnSignupCompleteCheckIcon()

                Spacer(modifier = Modifier.height(SignupCompleteTitleSpacing))

                Text(
                    text = uiState.title,
                    style = bookOnTypography.screenTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(AppSpacing.Small))

                Text(
                    text = uiState.message,
                    style = bookOnTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(SignupCompleteSummarySpacing))

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

                Spacer(modifier = Modifier.height(SignupCompleteBottomSpacing))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSignupCompleteScreenPreview() {
    BookOnTheme {
        BookOnSignupCompleteScreen(uiState = defaultSignupCompleteUiState(), onStartClick = {})
    }
}
