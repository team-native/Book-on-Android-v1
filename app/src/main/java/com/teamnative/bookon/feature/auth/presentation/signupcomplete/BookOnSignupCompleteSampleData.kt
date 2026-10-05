package com.teamnative.bookon.feature.auth.presentation.signupcomplete

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R

@Composable
internal fun defaultSignupCompleteUiState(
    isReadingMarathonLinked: Boolean = false,
    registeredName: String = "",
) = BookOnSignupCompleteUiState(
    title = stringResource(R.string.signup_complete_title),
    message = if (registeredName.isBlank()) {
        stringResource(R.string.signup_complete_generic_message)
    } else {
        stringResource(R.string.signup_complete_message, registeredName)
    },
    ownedBookCountText = "--",
    marathonStatusText = stringResource(
        if (isReadingMarathonLinked) R.string.status_linked else R.string.status_not_linked,
    ),
)
