package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnDropdownFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnOptionButtonUiModel

@Immutable
data class BookOnSignupUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val email: BookOnTextFieldUiModel,
    val name: BookOnTextFieldUiModel,
    val genderOptions: List<BookOnOptionButtonUiModel>,
    val department: BookOnDropdownFieldUiModel,
    val nextEnabled: Boolean = true,
)
