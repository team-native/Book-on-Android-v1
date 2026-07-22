package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnMarathonAgreementUiModel

@Immutable
data class BookOnReadingMarathonLinkUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val marathonId: BookOnTextFieldUiModel,
    val password: BookOnPasswordFieldUiModel,
    val agreement: BookOnMarathonAgreementUiModel,
    val linkEnabled: Boolean = true,
    val errorText: String? = null,
    val isLoading: Boolean = false,
)
