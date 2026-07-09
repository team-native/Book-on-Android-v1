package com.teamnative.bookon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teamnative.bookon.ui.Component.bar.BookOnBottomNavigationBar
import com.teamnative.bookon.ui.route.auth.BookOnLoginRoute
import com.teamnative.bookon.ui.route.auth.BookOnPasswordSetupRoute
import com.teamnative.bookon.ui.route.auth.BookOnReadingMarathonLinkRoute
import com.teamnative.bookon.ui.route.auth.BookOnReadingMarathonSignupRoute
import com.teamnative.bookon.ui.route.auth.BookOnSignupCompleteRoute
import com.teamnative.bookon.ui.route.auth.BookOnSignupRoute
import com.teamnative.bookon.ui.route.auth.BookOnVerificationCodeRoute
import com.teamnative.bookon.ui.screen.book.BookOnBookDetailRoute
import com.teamnative.bookon.ui.screen.book.BookOnSearchRoute
import com.teamnative.bookon.ui.screen.home.BookOnHomeRoute
import com.teamnative.bookon.ui.screen.home.BookOnNewBooksRoute
import com.teamnative.bookon.ui.screen.library.BookOnLibraryRoute
import com.teamnative.bookon.ui.screen.my.BookOnFavoriteBooksRoute
import com.teamnative.bookon.ui.screen.my.BookOnLoanHistoryRoute
import com.teamnative.bookon.ui.screen.my.BookOnMyRoute
import com.teamnative.bookon.ui.screen.ranking.BookOnRankingRoute

private sealed interface BookOnDestination {
    val route: String

    data object Login : BookOnDestination {
        override val route = "login"
    }

    data object Signup : BookOnDestination {
        override val route = "signup"
    }

    data object VerificationCode : BookOnDestination {
        override val route = "verificationCode"
    }

    data object PasswordSetup : BookOnDestination {
        override val route = "passwordSetup"
    }

    data object ReadingMarathonSignup : BookOnDestination {
        override val route = "readingMarathonSignup"
    }

    data object ReadingMarathonLink : BookOnDestination {
        override val route = "readingMarathonLink"
    }

    data object SignupComplete : BookOnDestination {
        override val route = "signupComplete"
    }

    data object Home : BookOnDestination {
        override val route = "home"
    }

    data object Ranking : BookOnDestination {
        override val route = "ranking"
    }

    data object Library : BookOnDestination {
        override val route = "library"
    }

    data object My : BookOnDestination {
        override val route = "my"
    }

    data object Search : BookOnDestination {
        override val route = "search"
    }

    data object BookDetail : BookOnDestination {
        override val route = "bookDetail"
    }

    data object NewBooks : BookOnDestination {
        override val route = "newBooks"
    }

    data object LoanHistory : BookOnDestination {
        override val route = "loanHistory"
    }

    data object Favorites : BookOnDestination {
        override val route = "favorites"
    }
}

private val mainDestinations = listOf(
    BookOnDestination.Home,
    BookOnDestination.Ranking,
    BookOnDestination.Library,
    BookOnDestination.My,
)

/**
 * 앱의 최상위 Navigation Host이다.
 * 로그인/회원가입 플로우와 메인 탭, 상세 화면 이동을 단일 NavController로 관리한다.
 */
@Composable
fun BookOnNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomBar: @Composable () -> Unit = {
        BookOnMainBottomBar(
            currentRoute = currentRoute,
            onDestinationSelected = navController::navigateToMainDestination,
        )
    }

    NavHost(
        navController = navController,
        startDestination = BookOnDestination.Login.route,
    ) {
        composable(BookOnDestination.Login.route) {
            BookOnLoginRoute(
                onLoginClick = { navController.navigateToHomeAndClearAuth() },
                onSignupClick = { navController.navigate(BookOnDestination.Signup.route) },
            )
        }
        composable(BookOnDestination.Signup.route) {
            BookOnSignupRoute(
                onBackClick = navController::navigateUp,
                onNextClick = { navController.navigate(BookOnDestination.VerificationCode.route) },
            )
        }
        composable(BookOnDestination.VerificationCode.route) {
            BookOnVerificationCodeRoute(
                onBackClick = navController::navigateUp,
                onConfirmClick = { navController.navigate(BookOnDestination.PasswordSetup.route) },
            )
        }
        composable(BookOnDestination.PasswordSetup.route) {
            BookOnPasswordSetupRoute(
                onBackClick = navController::navigateUp,
                onNextClick = { navController.navigate(BookOnDestination.ReadingMarathonSignup.route) },
            )
        }
        composable(BookOnDestination.ReadingMarathonSignup.route) {
            BookOnReadingMarathonSignupRoute(
                onBackClick = navController::navigateUp,
                onUseClick = { navController.navigate(BookOnDestination.ReadingMarathonLink.route) },
                onSkipClick = { navController.navigate(BookOnDestination.SignupComplete.route) },
            )
        }
        composable(BookOnDestination.ReadingMarathonLink.route) {
            BookOnReadingMarathonLinkRoute(
                onBackClick = navController::navigateUp,
                onSkipClick = { navController.navigate(BookOnDestination.SignupComplete.route) },
                onCompleteClick = { navController.navigate(BookOnDestination.SignupComplete.route) },
            )
        }
        composable(BookOnDestination.SignupComplete.route) {
            BookOnSignupCompleteRoute(
                onStartClick = { navController.navigateToHomeAndClearAuth() },
            )
        }
        composable(BookOnDestination.Home.route) {
            BookOnHomeRoute(
                bottomBar = bottomBar,
                onSearchClick = { navController.navigate(BookOnDestination.Search.route) },
                onNewBooksClick = { navController.navigate(BookOnDestination.NewBooks.route) },
                onNotificationClick = {
                    // TODO: 서버 알림 API 또는 알림 화면이 준비되면 이 이벤트에 연결한다.
                },
            )
        }
        composable(BookOnDestination.Ranking.route) {
            BookOnRankingRoute(bottomBar = bottomBar)
        }
        composable(BookOnDestination.Library.route) {
            BookOnLibraryRoute(
                bottomBar = bottomBar,
                onBookClick = { navController.navigate(BookOnDestination.BookDetail.route) },
            )
        }
        composable(BookOnDestination.My.route) {
            BookOnMyRoute(
                bottomBar = bottomBar,
                onLoanHistoryClick = { navController.navigate(BookOnDestination.LoanHistory.route) },
                onFavoriteClick = { navController.navigate(BookOnDestination.Favorites.route) },
                onLogoutRequest = {
                    // TODO: 로그아웃 API와 토큰 저장소가 준비되면 서버에 토큰을 반납하고 로컬 세션을 정리한 뒤 이동한다.
                    navController.navigateToLoginAndClearBackStack()
                },
            )
        }
        composable(BookOnDestination.Search.route) {
            BookOnSearchRoute(
                onBackClick = navController::navigateUp,
                onBookClick = { navController.navigate(BookOnDestination.BookDetail.route) },
            )
        }
        composable(BookOnDestination.BookDetail.route) {
            BookOnBookDetailRoute(
                onBackClick = navController::navigateUp,
            )
        }
        composable(BookOnDestination.NewBooks.route) {
            BookOnNewBooksRoute(
                onBackClick = navController::navigateUp,
            )
        }
        composable(BookOnDestination.LoanHistory.route) {
            BookOnLoanHistoryRoute(
                onBackClick = navController::navigateUp,
                onBookClick = { navController.navigate(BookOnDestination.BookDetail.route) },
            )
        }
        composable(BookOnDestination.Favorites.route) {
            BookOnFavoriteBooksRoute(
                onBackClick = navController::navigateUp,
                onBookClick = { navController.navigate(BookOnDestination.BookDetail.route) },
            )
        }
    }
}

@Composable
private fun BookOnMainBottomBar(
    currentRoute: String?,
    onDestinationSelected: (BookOnDestination) -> Unit,
) {
    val selectedIndex = mainDestinations
        .indexOfFirst { destination -> destination.route == currentRoute }
        .coerceAtLeast(0)

    BookOnBottomNavigationBar(
        items = defaultBookOnNavigationItems(),
        selectedIndex = selectedIndex,
        onItemClick = { index ->
            mainDestinations.getOrNull(index)?.let(onDestinationSelected)
        },
    )
}

/**
 * 하단 탭 이동은 기존 탭 상태를 복원하고 같은 목적지가 back stack에 중복 누적되지 않게 처리한다.
 */
private fun NavHostController.navigateToMainDestination(destination: BookOnDestination) {
    navigate(destination.route) {
        popUpTo(BookOnDestination.Home.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * 임시 인증 완료 후 인증 플로우를 back stack에서 제거하고 홈으로 진입한다.
 */
private fun NavHostController.navigateToHomeAndClearAuth() {
    navigate(BookOnDestination.Home.route) {
        popUpTo(BookOnDestination.Login.route) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

/**
 * 로그아웃 후 앱의 시작 화면으로 이동하고 이전 인증 화면과 메인 화면 기록을 제거한다.
 */
private fun NavHostController.navigateToLoginAndClearBackStack() {
    navigate(BookOnDestination.Login.route) {
        popUpTo(graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
