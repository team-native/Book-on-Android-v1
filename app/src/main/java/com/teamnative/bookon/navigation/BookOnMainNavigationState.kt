package com.teamnative.bookon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator

/**
 * 하단 탭(Home/Ranking/Library/My)마다 독립된 back stack을 유지한다.
 * 탭을 전환해도 이전에 그 탭에서 쌓아 둔 화면들이 그대로 보존된다.
 */
internal class BookOnMainNavigationState(
    val startRoute: BookOnDestination,
    private val topLevelRoutes: List<BookOnDestination>,
    topLevelIndex: MutableState<Int>,
    val backStacks: Map<BookOnDestination, NavBackStack<NavKey>>,
) {
    private var topLevelIndex: Int by topLevelIndex

    /** [BookOnDestination]은 kotlinx.serialization 대상이라 rememberSaveable 기본 Saver로 못 담기 때문에 index로 저장한다. */
    var topLevelRoute: BookOnDestination
        get() = topLevelRoutes[topLevelIndex]
        set(value) {
            val index = topLevelRoutes.indexOf(value)
            if (index >= 0) topLevelIndex = index
        }

    val currentBackStack: NavBackStack<NavKey>
        get() = backStacks.getValue(topLevelRoute)
}

@Composable
internal fun rememberBookOnMainNavigationState(
    startRoute: BookOnDestination,
    topLevelRoutes: List<BookOnDestination>,
): BookOnMainNavigationState {
    val topLevelIndex = rememberSaveable { mutableStateOf(topLevelRoutes.indexOf(startRoute).coerceAtLeast(0)) }
    val backStacks = topLevelRoutes.associateWith { route -> rememberNavBackStack(route) }

    return remember(startRoute, topLevelRoutes) {
        BookOnMainNavigationState(
            startRoute = startRoute,
            topLevelRoutes = topLevelRoutes,
            topLevelIndex = topLevelIndex,
            backStacks = backStacks,
        )
    }
}

/** 현재 활성 탭의 back stack을 [NavEntry] 목록으로 변환한다. 엔트리별 ViewModel/상태 저장 범위를 함께 부여한다. */
@Composable
internal fun BookOnMainNavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val viewModelStoreDecorator = rememberViewModelStoreNavEntryDecorator<NavKey>()
    val savedStateDecorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(savedStateDecorator, viewModelStoreDecorator),
            entryProvider = entryProvider,
        )
    }

    return decoratedEntries.getValue(topLevelRoute).toMutableStateList()
}

/** 탭 전환/화면 push/pop을 담당한다. NavController를 대체한다. */
internal class BookOnMainNavigator(private val state: BookOnMainNavigationState) {

    /** 하단 탭 선택 시 호출한다. 같은 탭을 다시 누르면 아무 일도 하지 않는다. */
    fun navigateToTab(destination: BookOnDestination) {
        if (destination in state.backStacks.keys) {
            state.topLevelRoute = destination
        }
    }

    /** 현재 탭의 back stack에 화면을 push한다. */
    fun push(destination: BookOnDestination) {
        state.currentBackStack.add(destination)
    }

    /** 시스템/UI 뒤로가기: 현재 탭 back stack에서 pop하고, 탭의 시작 화면까지 pop되면 시작 탭으로 되돌린다. */
    fun goBack() {
        val stack = state.currentBackStack
        if (stack.size > 1) {
            stack.removeLastOrNull()
        } else if (state.topLevelRoute != state.startRoute) {
            state.topLevelRoute = state.startRoute
        }
    }
}
