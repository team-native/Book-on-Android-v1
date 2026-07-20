package com.teamnative.bookon.feature.auth.presentation.signupcomplete

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R

@Composable
internal fun sampleSignupCompleteUiState(
    isReadingMarathonLinked: Boolean = true,
) = BookOnSignupCompleteUiState(
    title = stringResource(R.string.signup_complete_title),
    message = stringResource(R.string.signup_complete_message, "홍길동"),
    ownedBookCountText = "4,218",
    marathonStatusText = stringResource(
        if (isReadingMarathonLinked) R.string.status_linked else R.string.status_not_linked,
    ),
)
