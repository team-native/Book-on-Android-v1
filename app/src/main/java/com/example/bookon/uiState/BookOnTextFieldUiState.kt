package com.example.bookon.uiState

import androidx.compose.runtime.Immutable

@Immutable
data class BookOnTextFieldUiState(
    val value: String,
    val label: String? = null,
    val placeholder: String = "",
    val errorText: String? = null,
    val isError: Boolean = errorText != null,
    val enabled: Boolean = true,
)

@Immutable
data class BookOnPasswordFieldUiState(
    val value: String,
    val label: String? = null,
    val placeholder: String = "",
    val errorText: String? = null,
    val enabled: Boolean = true,
)
