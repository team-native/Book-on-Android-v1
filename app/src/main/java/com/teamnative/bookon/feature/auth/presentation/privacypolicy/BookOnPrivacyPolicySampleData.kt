package com.teamnative.bookon.feature.auth.presentation.privacypolicy

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R

@Composable
internal fun defaultPrivacyPolicyUiState() = BookOnPrivacyPolicyUiState(
    stepText = stringResource(R.string.signup_step_2),
    title = stringResource(R.string.privacy_policy_title),
    sections = listOf(
        BookOnPrivacyPolicySectionUiModel(
            stringResource(R.string.privacy_collected_items_title),
            stringResource(R.string.privacy_collected_items),
        ),
        BookOnPrivacyPolicySectionUiModel(
            stringResource(R.string.privacy_purpose_title),
            "${stringResource(R.string.privacy_purpose_account)}\n${stringResource(R.string.privacy_purpose_notice)}",
        ),
        BookOnPrivacyPolicySectionUiModel(
            stringResource(R.string.privacy_retention_title),
            stringResource(R.string.privacy_retention_until_withdrawal),
        ),
    ),
    notice = stringResource(R.string.privacy_refusal_notice),
)
