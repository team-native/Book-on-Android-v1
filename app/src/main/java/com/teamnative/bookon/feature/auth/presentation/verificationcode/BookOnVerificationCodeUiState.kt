package com.teamnative.bookon.feature.auth.presentation.verificationcode

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnVerificationCodeUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val code: String,
    val expireText: String,
    val errorText: String? = null,
    val confirmEnabled: Boolean = true,
)
