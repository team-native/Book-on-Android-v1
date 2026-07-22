package com.teamnative.bookon.feature.auth.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnMarathonAgreementUiModel(
    val text: String,
    val checked: Boolean,
    val enabled: Boolean = true,
)
