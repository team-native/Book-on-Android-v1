package com.teamnative.bookon.feature.auth.presentation.passwordreset

/** 비밀번호 재설정 화면에서 Route로 전달하는 사용자 의도다. */
sealed interface BookOnPasswordResetScreenEvent {
    data object BackClicked : BookOnPasswordResetScreenEvent
    data class EmailChanged(val email: String) : BookOnPasswordResetScreenEvent
    data class VerificationCodeChanged(val code: String) : BookOnPasswordResetScreenEvent
    data class PasswordChanged(val password: String) : BookOnPasswordResetScreenEvent
    data class PasswordConfirmChanged(val passwordConfirm: String) : BookOnPasswordResetScreenEvent
    data object ResendClicked : BookOnPasswordResetScreenEvent
    data object ContinueClicked : BookOnPasswordResetScreenEvent
}
