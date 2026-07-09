package com.example.bookon.ui.app

import androidx.compose.runtime.Composable
import com.example.bookon.navigation.BookOnNavHost

/**
 * 앱의 최상위 Compose 진입점이다.
 * 화면 전환 정의는 navigation 패키지의 BookOnNavHost가 담당한다.
 */
@Composable
fun BookOnApp() {
    BookOnNavHost()
}
