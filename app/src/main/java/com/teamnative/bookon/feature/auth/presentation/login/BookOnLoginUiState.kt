package com.teamnative.bookon.feature.auth.presentation.login

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

sealed interface BookOnLoginError {
    data object RequestFailed : BookOnLoginError
}

@Immutable
data class BookOnLoginUiState(
    val title: String,
    val email: BookOnTextFieldUiModel,
    val password: BookOnPasswordFieldUiModel,
    val loginEnabled: Boolean = true,
    val isSubmitting: Boolean = false,
    val hasMissingCredentials: Boolean = false,
    val loginError: BookOnLoginError? = null,
)
