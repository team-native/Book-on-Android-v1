package com.teamnative.bookon.navigation

import androidx.navigation.NavHostController

/** 메인 탭 상태를 복원하고 동일 목적지가 중복 누적되지 않게 이동한다. */
internal fun NavHostController.navigateToMainDestination(destination: BookOnDestination) {
    navigate(destination.route) {
        popUpTo(BookOnDestination.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** 인증 플로우를 back stack에서 제거하고 홈으로 진입한다. */
internal fun NavHostController.navigateToHomeAndClearAuth() {
    navigate(BookOnDestination.Home.route) {
        popUpTo(BookOnDestination.Login.route) { inclusive = true }
        launchSingleTop = true
    }
}

/** 로그아웃 후 로그인 화면만 남도록 기존 back stack을 제거한다. */
internal fun NavHostController.navigateToLoginAndClearBackStack() {
    navigate(BookOnDestination.Login.route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}
