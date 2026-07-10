package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnMarathonAgreementUiModel

@Composable
internal fun sampleReadingMarathonLinkUiState() = BookOnReadingMarathonLinkUiState(
    stepText = stringResource(R.string.signup_step_3),
    title = stringResource(R.string.reading_marathon_link_title),
    description = stringResource(R.string.reading_marathon_login_description),
    marathonId = BookOnTextFieldUiModel(
        value = "",
        label = stringResource(R.string.reading_marathon_id),
        placeholder = "s20000@gsm.hs.kr",
    ),
    password = BookOnPasswordFieldUiModel(
        value = "",
        label = stringResource(R.string.password),
        placeholder = stringResource(R.string.password),
    ),
    agreement = BookOnMarathonAgreementUiModel(
        text = stringResource(R.string.reading_marathon_third_party_agreement),
        checked = false,
    ),
)
