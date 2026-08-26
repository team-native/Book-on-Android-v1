package com.teamnative.bookon.feature.my.presentation.main

/** 내 서재 Screen이 Route에 전달하는 사용자 의도이다. */
sealed interface BookOnMyScreenEvent {
    data class MenuClicked(val menuIndex: Int) : BookOnMyScreenEvent
    data object ProfileImageEditClicked : BookOnMyScreenEvent
    data object ReadingMarathonLinkRequested : BookOnMyScreenEvent
    data object LogoutClicked : BookOnMyScreenEvent
    data object RetryClicked : BookOnMyScreenEvent
}
