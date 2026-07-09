package com.teamnative.bookon.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.teamnative.bookon.R

@Immutable
data class BookOnNavigationItem(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
)

/**
 * 앱 하단 주요 목적지 4개를 기본 순서대로 제공한다.
 * 반환된 항목은 BookOnBottomNavigationBar에서 선택 상태에 맞춰 색상이 적용된다.
 */
fun defaultBookOnNavigationItems(): List<BookOnNavigationItem> = listOf(
    BookOnNavigationItem(
        labelRes = R.string.nav_home,
        iconRes = R.drawable.navigation_home,
    ),
    BookOnNavigationItem(
        labelRes = R.string.nav_ranking,
        iconRes = R.drawable.navigation_rank,
    ),
    BookOnNavigationItem(
        labelRes = R.string.nav_library,
        iconRes = R.drawable.navigation_library,
    ),
    BookOnNavigationItem(
        labelRes = R.string.nav_my,
        iconRes = R.drawable.navigation_my,
    ),
)
