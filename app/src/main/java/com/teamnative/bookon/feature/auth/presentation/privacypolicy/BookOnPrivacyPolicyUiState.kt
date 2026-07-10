package com.teamnative.bookon.feature.auth.presentation.privacypolicy

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnPrivacyPolicyUiState(
    val stepText: String,
    val title: String,
    val sections: List<BookOnPrivacyPolicySectionUiModel>,
    val notice: String,
)

@Immutable
data class BookOnPrivacyPolicySectionUiModel(
    val title: String,
    val body: String,
)
