package com.teamnative.bookon.navigation

import androidx.compose.runtime.Composable
import com.teamnative.bookon.core.ui.component.bar.BookOnBottomNavigationBar

/** 현재 메인 목적지에 맞춰 선택 상태가 반영된 하단 탭을 표시한다. */
@Composable
internal fun BookOnMainBottomBar(
    current: BookOnDestination?,
    onDestinationSelected: (BookOnDestination) -> Unit,
) {
    val selectedIndex = mainDestinations
        .indexOfFirst { destination -> destination == current }
        .coerceAtLeast(0)

    BookOnBottomNavigationBar(
        items = defaultBookOnNavigationItems(),
        selectedIndex = selectedIndex,
        onItemClick = { index ->
            mainDestinations.getOrNull(index)?.let(onDestinationSelected)
        },
    )
}
