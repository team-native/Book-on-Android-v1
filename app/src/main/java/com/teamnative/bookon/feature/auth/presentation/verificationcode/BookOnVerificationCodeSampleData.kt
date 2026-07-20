package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R

@Composable
internal fun sampleVerificationCodeUiState() = BookOnVerificationCodeUiState(
    stepText = stringResource(R.string.signup_step_1),
    title = stringResource(R.string.verification_code_title),
    description = stringResource(R.string.verification_code_description, "s20000@gsm.hs.kr"),
    code = "",
    expireText = stringResource(R.string.verification_code_expire, "04:52"),
)
