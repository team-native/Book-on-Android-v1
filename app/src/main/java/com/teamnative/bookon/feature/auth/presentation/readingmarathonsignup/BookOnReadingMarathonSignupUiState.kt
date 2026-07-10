package com.teamnative.bookon.feature.auth.presentation.readingmarathonsignup

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnReadingMarathonSignupUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val useTitle: String,
    val useDescription: String,
    val benefitText: String,
    val laterNotice: String? = null,
)
