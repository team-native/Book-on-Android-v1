package com.teamnative.bookon.feature.auth.presentation.passwordsetup

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel

@Immutable
data class BookOnPasswordSetupUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val password: BookOnPasswordFieldUiModel,
    val passwordConfirm: BookOnPasswordFieldUiModel,
    val ruleTitle: String,
    val ruleText: String,
    val privacyChecked: Boolean,
    val privacyPolicyExpanded: Boolean = false,
    val nextEnabled: Boolean = true,
)
