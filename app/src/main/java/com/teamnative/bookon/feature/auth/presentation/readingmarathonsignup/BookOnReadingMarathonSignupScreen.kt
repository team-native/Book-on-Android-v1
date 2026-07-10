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
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSkipTextButton
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnMarathonNoticeCard
import com.teamnative.bookon.feature.auth.presentation.component.BookOnMarathonToggleCard

/**
 * 독서마라톤 안내 화면은 연동 이점과 건너뛰기/다음 액션을 표시한다.
 */
@Composable
fun BookOnReadingMarathonSignupScreen(
    uiState: BookOnReadingMarathonSignupUiState,
    onBackClick: () -> Unit,
    onUseClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var progressAnimating by remember { mutableStateOf(true) }

    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_next),
                onClick = onUseClick,
                enabled = !progressAnimating,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Item))
            BookOnSkipTextButton(
                text = stringResource(R.string.skip_reading_marathon),
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
            onClick = { if (!progressAnimating) onUseClick() },
        )
        BookOnMarathonNoticeCard(text = uiState.benefitText)
    }
}
