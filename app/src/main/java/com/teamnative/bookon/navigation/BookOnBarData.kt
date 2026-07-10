package com.teamnative.bookon.navigation

import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnNavigationItemUiModel

/**
 * 앱 하단 주요 목적지 4개를 기본 순서대로 제공한다.
 * 반환된 항목은 BookOnBottomNavigationBar에서 선택 상태에 맞춰 색상이 적용된다.
 */
fun defaultBookOnNavigationItems(): List<BookOnNavigationItemUiModel> = listOf(
    BookOnNavigationItemUiModel(
        labelRes = R.string.nav_home,
        iconRes = R.drawable.navigation_home,
    ),
    BookOnNavigationItemUiModel(
        labelRes = R.string.nav_ranking,
        iconRes = R.drawable.navigation_rank,
    ),
    BookOnNavigationItemUiModel(
        labelRes = R.string.nav_library,
        iconRes = R.drawable.navigation_library,
    ),
    BookOnNavigationItemUiModel(
        labelRes = R.string.nav_my,
        iconRes = R.drawable.navigation_my,
    ),
)
