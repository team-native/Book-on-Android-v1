package com.teamnative.bookon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teamnative.bookon.feature.auth.presentation.login.BookOnLoginRoute
import com.teamnative.bookon.feature.auth.presentation.passwordsetup.BookOnPasswordSetupRoute
import com.teamnative.bookon.feature.auth.presentation.readingmarathonlink.BookOnReadingMarathonLinkRoute
import com.teamnative.bookon.feature.auth.presentation.readingmarathonsignup.BookOnReadingMarathonSignupRoute
import com.teamnative.bookon.feature.auth.presentation.signupcomplete.BookOnSignupCompleteRoute
import com.teamnative.bookon.feature.auth.presentation.signup.BookOnSignupRoute
import com.teamnative.bookon.feature.auth.presentation.verificationcode.BookOnVerificationCodeRoute
import com.teamnative.bookon.feature.book.presentation.detail.BookOnBookDetailRoute
import com.teamnative.bookon.feature.book.presentation.search.BookOnSearchRoute
import com.teamnative.bookon.feature.home.presentation.home.BookOnHomeRoute
import com.teamnative.bookon.feature.home.presentation.newbooks.BookOnNewBooksRoute
import com.teamnative.bookon.feature.library.presentation.library.BookOnLibraryRoute
import com.teamnative.bookon.feature.my.presentation.favorites.BookOnFavoriteBooksRoute
import com.teamnative.bookon.feature.my.presentation.loanhistory.BookOnLoanHistoryRoute
import com.teamnative.bookon.feature.my.presentation.main.BookOnMyRoute
import com.teamnative.bookon.feature.ranking.presentation.ranking.BookOnRankingRoute

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
