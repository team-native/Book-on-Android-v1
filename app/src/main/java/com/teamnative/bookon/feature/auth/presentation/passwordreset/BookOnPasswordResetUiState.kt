package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

/** UI 전용 비밀번호 재설정 흐름에서 현재 입력 단계와 표시 데이터를 보관한다. */
@Immutable
data class BookOnPasswordResetUiState(
    val step: BookOnPasswordResetStep,
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

/** 비밀번호 재설정에서 사용자에게 노출되는 세 단계다. */
enum class BookOnPasswordResetStep(val index: Int) {
    Email(index = 1),
    Verification(index = 2),
    Password(index = 3),
}
