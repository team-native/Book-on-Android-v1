package com.teamnative.bookon.feature.auth.presentation.readingmarathonsignup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSkipTextButton
import com.teamnative.bookon.feature.auth.presentation.component.skipReadingMarathonAnnotatedString
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnMarathonNoticeCard
import com.teamnative.bookon.feature.auth.presentation.component.BookOnMarathonToggleCard
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme

/**
 * 독서마라톤 안내 화면은 연동 이점과 건너뛰기/다음 액션을 표시한다.
 */
@Composable
fun BookOnReadingMarathonSignupScreen(
    uiState: BookOnReadingMarathonSignupUiState,
    onBackClick: () -> Unit,
    onLinkChange: (Boolean) -> Unit,
    onContinueClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var progressAnimating by remember { mutableStateOf(true) }

    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(if (uiState.isLinked) R.string.action_next else R.string.complete_signup_with_link),
                onClick = onContinueClick,
                enabled = uiState.isLinked && !progressAnimating,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            BookOnSkipTextButton(
                text = skipReadingMarathonAnnotatedString(),
                onClick = onSkipClick,
            )
        },
    ) {

        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))

        BookOnSignupStepHeader(
            step = 3,
            title = uiState.title,
            description = uiState.description,
            onProgressAnimationRunningChange = { progressAnimating = it },
        )
        BookOnMarathonToggleCard(
            title = uiState.useTitle,
            description = uiState.useDescription,
            checked = uiState.isLinked,
            onCheckedChange = { isLinked ->
                if (!progressAnimating) onLinkChange(isLinked)
            },
        )
        BookOnMarathonNoticeCard(
            text = if (uiState.isLinked) uiState.benefitText else uiState.laterNotice,
            isLinked = uiState.isLinked,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnReadingMarathonSignupScreenPreview() {
    BookOnTheme {
        BookOnReadingMarathonSignupScreen(
            uiState = defaultReadingMarathonSignupUiState(),
            onBackClick = {}, onLinkChange = {}, onContinueClick = {}, onSkipClick = {},
        )
    }
}
