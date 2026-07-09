package com.teamnative.bookon.uiState.marathon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.teamnative.bookon.R

@Immutable
data class BookOnMarathonLinkCardUiState(
    val title: String,
    val description: String,
    @param:DrawableRes val iconRes: Int = R.drawable.marathon_logo,
    val iconContentDescription: String? = null,
)

@Immutable
data class BookOnMarathonAgreementUiState(
    val text: String,
    val checked: Boolean,
    val enabled: Boolean = true,
)

@Immutable
data class BookOnMarathonCircleActionUiState(
    @param:DrawableRes val iconRes: Int,
    val contentDescription: String,
    val enabled: Boolean = true,
)
