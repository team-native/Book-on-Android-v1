package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

internal const val PasswordResetEmailStep = 1
internal const val PasswordResetVerificationStep = 2
internal const val PasswordResetNewPasswordStep = 3
internal const val PasswordResetVerificationCodeLength = 6

/** UI 전용 비밀번호 재설정 흐름에서 현재 입력 단계와 표시 데이터를 보관한다. */
@Immutable
data class BookOnPasswordResetUiState(
    val title: String,
    val description: String,
    val email: BookOnTextFieldUiModel,
    val verificationCode: String,
    val password: BookOnPasswordFieldUiModel,
    val passwordConfirm: BookOnPasswordFieldUiModel,
    val nextEnabled: Boolean,
    val errorText: String? = null,
    val isLoading: Boolean = false,
)
