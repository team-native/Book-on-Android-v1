package com.teamnative.bookon.feature.auth.presentation.signup

/** 회원가입 Screen이 Route에 전달하는 사용자 의도이다. */
sealed interface BookOnSignupScreenEvent {
    data object BackClicked : BookOnSignupScreenEvent
    data class EmailChanged(val email: String) : BookOnSignupScreenEvent
    data class NameChanged(val name: String) : BookOnSignupScreenEvent
    data class GenderSelected(val gender: BookOnGender) : BookOnSignupScreenEvent
    data object DepartmentClicked : BookOnSignupScreenEvent
    data object NextClicked : BookOnSignupScreenEvent
}
