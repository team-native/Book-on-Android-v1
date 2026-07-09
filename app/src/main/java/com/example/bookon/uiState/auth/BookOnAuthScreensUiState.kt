package com.example.bookon.uiState.auth

import androidx.compose.runtime.Immutable
import com.example.bookon.uiState.BookOnPasswordFieldUiState
import com.example.bookon.uiState.BookOnTextFieldUiState
import com.example.bookon.uiState.marathon.BookOnMarathonAgreementUiState

@Immutable
data class BookOnLoginUiState(
    val title: String,
    val email: BookOnTextFieldUiState,
    val password: BookOnPasswordFieldUiState,
    val loginEnabled: Boolean = true,
)

@Immutable
data class BookOnSignupUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val email: BookOnTextFieldUiState,
    val name: BookOnTextFieldUiState,
    val genderOptions: List<BookOnOptionButtonUiState>,
    val department: BookOnDropdownFieldUiState,
    val nextEnabled: Boolean = true,
)

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

@Immutable
data class BookOnPasswordSetupUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val password: BookOnPasswordFieldUiState,
    val passwordConfirm: BookOnPasswordFieldUiState,
    val ruleTitle: String,
    val ruleText: String,
    val privacyChecked: Boolean,
    val nextEnabled: Boolean = true,
)

@Immutable
data class BookOnPrivacyPolicyUiState(
    val stepText: String,
    val title: String,
    val sections: List<BookOnPrivacyPolicySectionUiState>,
    val notice: String,
)

@Immutable
data class BookOnPrivacyPolicySectionUiState(
    val title: String,
    val body: String,
)

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

@Immutable
data class BookOnReadingMarathonLinkUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val marathonId: BookOnTextFieldUiState,
    val password: BookOnPasswordFieldUiState,
    val agreement: BookOnMarathonAgreementUiState,
    val linkEnabled: Boolean = true,
)

@Immutable
data class BookOnSignupCompleteUiState(
    val title: String,
    val message: String,
    val ownedBookCountText: String,
    val marathonStatusText: String,
)
