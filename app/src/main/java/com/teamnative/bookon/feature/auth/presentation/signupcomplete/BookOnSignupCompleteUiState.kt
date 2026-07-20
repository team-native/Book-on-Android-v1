package com.teamnative.bookon.feature.auth.presentation.signupcomplete

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnSignupCompleteUiState(
    val title: String,
    val message: String,
    val ownedBookCountText: String,
    val marathonStatusText: String,
)
