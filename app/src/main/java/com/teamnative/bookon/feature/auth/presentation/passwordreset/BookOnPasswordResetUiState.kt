package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import java.util.Locale

internal const val PasswordResetEmailStep = 1
internal const val PasswordResetVerificationStep = 2
internal const val PasswordResetNewPasswordStep = 3
internal const val PasswordResetVerificationCodeLength = 6
private const val SECONDS_PER_MINUTE = 60

/** UI 전용 비밀번호 재설정 흐름에서 현재 입력 단계와 표시 데이터를 보관한다. */
@Immutable
data class BookOnPasswordResetUiState(
    val title: String,
    val description: String,
    val email: BookOnTextFieldUiModel,
    val verificationCode: String,
    val verificationRemainingSeconds: Long,
    val password: BookOnPasswordFieldUiModel,
    val passwordConfirm: BookOnPasswordFieldUiModel,
    val nextEnabled: Boolean,
    val errorText: String? = null,
    val isLoading: Boolean = false,
)

/** 인증코드의 남은 초를 화면에 표시할 `mm:ss` 형식으로 변환한다. */
internal fun formatPasswordResetRemainingTime(remainingSeconds: Long): String {
    val normalizedSeconds = remainingSeconds.coerceAtLeast(0)
    val minutes = normalizedSeconds / SECONDS_PER_MINUTE
    val seconds = normalizedSeconds % SECONDS_PER_MINUTE

    return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
}
