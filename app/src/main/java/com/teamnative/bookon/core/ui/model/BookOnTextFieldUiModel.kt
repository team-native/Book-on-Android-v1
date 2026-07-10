package com.teamnative.bookon.core.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnTextFieldUiModel(
    val value: String,
    val label: String? = null,
    val placeholder: String = "",
    val suffixText: String? = null,
    val errorText: String? = null,
    val isError: Boolean = errorText != null,
    val enabled: Boolean = true,
)

@Immutable
data class BookOnPasswordFieldUiModel(
    val value: String,
    val label: String? = null,
    val placeholder: String = "",
    val errorText: String? = null,
    val enabled: Boolean = true,
)
