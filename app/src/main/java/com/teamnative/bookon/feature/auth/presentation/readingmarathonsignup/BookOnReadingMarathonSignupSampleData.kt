package com.teamnative.bookon.feature.auth.presentation.readingmarathonsignup

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R

@Composable
internal fun sampleReadingMarathonSignupUiState() = BookOnReadingMarathonSignupUiState(
    stepText = stringResource(R.string.signup_step_3),
    title = stringResource(R.string.reading_marathon),
    description = stringResource(R.string.reading_marathon_signup_description),
    useTitle = stringResource(R.string.reading_marathon_use),
    useDescription = stringResource(R.string.reading_marathon_use_description),
    benefitText = stringResource(R.string.reading_marathon_link_benefit),
    laterNotice = stringResource(R.string.reading_marathon_later_notice),
)
