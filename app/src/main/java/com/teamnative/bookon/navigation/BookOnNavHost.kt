package com.teamnative.bookon.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.bookon.feature.auth.presentation.signup.BookOnRegistrationViewModel
import com.teamnative.bookon.feature.auth.presentation.login.BookOnLoginRoute
import com.teamnative.bookon.feature.auth.presentation.passwordsetup.BookOnPasswordSetupRoute
import com.teamnative.bookon.feature.auth.presentation.passwordreset.BookOnPasswordResetRoute
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
import com.teamnative.bookon.core.designsystem.theme.AppAnimationDuration

private const val SignupProgressInitialStepKey = "signup_progress_initial_step"
private const val PasswordSetupStep = 2
private const val ReadingMarathonSignupStep = 3
private const val ReadingMarathonLinkFromMyKey = "reading_marathon_link_from_my"

/**
 * 앱의 최상위 Navigation Host이다.
 * 로그인/회원가입 플로우와 메인 탭, 상세 화면 이동을 단일 NavController로 관리한다.
 */
@Composable
fun BookOnNavHost(isInitiallyAuthenticated: Boolean, onLogout: () -> Unit) {
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
        startDestination = if (isInitiallyAuthenticated) {
            BookOnDestination.Home.route
        } else {
            BookOnDestination.Login.route
        },
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(BookOnDestination.Login.route) {
            BookOnLoginRoute(
                onLoginClick = { navController.navigateToHomeAndClearAuth() },
                onSignupClick = { navController.navigate(BookOnDestination.Signup.route) },
                onForgotPasswordClick = { navController.navigate(BookOnDestination.PasswordReset.route) },
            )
        }
        composable(BookOnDestination.PasswordReset.route) {
            BookOnPasswordResetRoute(onNavigateBack = navController::navigateUp)
        }
        composable(BookOnDestination.Signup.route) { backStackEntry ->
            val initialProgressStep by backStackEntry.savedStateHandle
                .getStateFlow<Int?>(SignupProgressInitialStepKey, null)
                .collectAsStateWithLifecycle()
            val registrationViewModel: BookOnRegistrationViewModel = hiltViewModel()
            BookOnSignupRoute(
                initialProgressStep = initialProgressStep,
                onBackClick = navController::navigateUp,
                onNextClick = { navController.navigate(BookOnDestination.PasswordSetup.route) },
                viewModel = registrationViewModel,
            )
        }
        composable(BookOnDestination.PasswordSetup.route) { backStackEntry ->
            val initialProgressStep by backStackEntry.savedStateHandle
                .getStateFlow<Int?>(SignupProgressInitialStepKey, null)
                .collectAsStateWithLifecycle()
            val registrationEntry = remember(backStackEntry) { navController.getBackStackEntry(BookOnDestination.Signup.route) }
            val registrationViewModel: BookOnRegistrationViewModel = hiltViewModel(registrationEntry)
            BookOnPasswordSetupRoute(
                initialProgressStep = initialProgressStep,
                onBackClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(SignupProgressInitialStepKey, PasswordSetupStep)
                    navController.navigateUp()
                },
                onNextClick = {
                    navController.navigate(BookOnDestination.VerificationCode.route)
                    // 비밀번호와 인증번호는 동일한 2단계이므로 게이지 애니메이션을 재생하지 않는다.
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(SignupProgressInitialStepKey, PasswordSetupStep)
                },
                viewModel = registrationViewModel,
            )
        }
        composable(BookOnDestination.VerificationCode.route) { backStackEntry ->
            val initialProgressStep by backStackEntry.savedStateHandle
                .getStateFlow<Int?>(SignupProgressInitialStepKey, null)
                .collectAsStateWithLifecycle()
            val registrationEntry = remember(backStackEntry) { navController.getBackStackEntry(BookOnDestination.Signup.route) }
            val registrationViewModel: BookOnRegistrationViewModel = hiltViewModel(registrationEntry)
            BookOnVerificationCodeRoute(
                initialProgressStep = initialProgressStep,
                onBackClick = {
                    // 비밀번호 설정과 인증번호는 모두 2단계이므로 게이지 값을 유지한 채 이전 화면으로 돌아간다.
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(SignupProgressInitialStepKey, PasswordSetupStep)
                    navController.navigateUp()
                },
                onConfirmClick = { navController.navigate(BookOnDestination.ReadingMarathonSignup.route) },
                viewModel = registrationViewModel,
            )
        }
        composable(BookOnDestination.ReadingMarathonSignup.route) {
            BookOnReadingMarathonSignupRoute(
                onBackClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(SignupProgressInitialStepKey, ReadingMarathonSignupStep)
                    navController.navigateUp()
                },
                onUseClick = {
                    navController.navigate(
                        BookOnDestination.ReadingMarathonLink.route,
                    )
                },
                onSkipClick = {
                    navController.navigate(
                        BookOnDestination.SignupComplete.createRoute(isReadingMarathonLinked = false),
                    )
                },
            )
        }
        composable(
            route = BookOnDestination.ReadingMarathonLink.route,
            enterTransition = {
                if (initialState.destination.route == BookOnDestination.My.route) {
                    slideInHorizontally(
                        animationSpec = tween(AppAnimationDuration.Navigation),
                        initialOffsetX = { fullWidth -> fullWidth },
                    )
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = {
                if (targetState.destination.route == BookOnDestination.My.route) {
                    slideOutHorizontally(
                        animationSpec = tween(AppAnimationDuration.Navigation),
                        targetOffsetX = { fullWidth -> fullWidth },
                    )
                } else {
                    ExitTransition.None
                }
            },
        ) {
            val openedFromMy = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Boolean>(ReadingMarathonLinkFromMyKey) ?: false
            BookOnReadingMarathonLinkRoute(
                onBackClick = navController::navigateUp,
                onSkipClick = {
                    if (openedFromMy) {
                        navController.navigateUp()
                    } else {
                        navController.navigate(
                            BookOnDestination.SignupComplete.createRoute(isReadingMarathonLinked = false),
                        )
                    }
                },
                onCompleteClick = {
                    if (openedFromMy) {
                        navController.navigateUp()
                    } else {
                        navController.navigate(
                            BookOnDestination.SignupComplete.createRoute(isReadingMarathonLinked = true),
                        )
                    }
                },
            )
        }
        composable(
            route = BookOnDestination.SignupComplete.route,
            arguments = listOf(
                navArgument(BookOnDestination.SignupComplete.isReadingMarathonLinkedArgument) {
                    type = NavType.BoolType
                },
            ),
        ) { backStackEntry ->
            val isReadingMarathonLinked = backStackEntry.arguments?.getBoolean(
                BookOnDestination.SignupComplete.isReadingMarathonLinkedArgument,
            ) ?: false
            BookOnSignupCompleteRoute(
                isReadingMarathonLinked = isReadingMarathonLinked,
                onStartClick = { navController.navigateToLoginAndClearBackStack() },
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
                onBookClick = { bookId -> navController.navigate(BookOnDestination.BookDetail.createRoute(bookId)) },
            )
        }
        composable(
            route = BookOnDestination.My.route,
            exitTransition = {
                if (targetState.destination.route == BookOnDestination.ReadingMarathonLink.route) {
                    slideOutHorizontally(
                        animationSpec = tween(AppAnimationDuration.Navigation),
                        targetOffsetX = { fullWidth -> -fullWidth },
                    )
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == BookOnDestination.ReadingMarathonLink.route) {
                    slideInHorizontally(
                        animationSpec = tween(AppAnimationDuration.Navigation),
                        initialOffsetX = { fullWidth -> -fullWidth },
                    )
                } else {
                    EnterTransition.None
                }
            },
        ) {
            BookOnMyRoute(
                bottomBar = bottomBar,
                onPasswordChangeClick = {
                    navController.navigate(BookOnDestination.PasswordReset.route)
                },
                onLoanHistoryClick = { navController.navigate(BookOnDestination.LoanHistory.route) },
                onFavoriteClick = { navController.navigate(BookOnDestination.Favorites.route) },
                onReadingMarathonLinkClick = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(ReadingMarathonLinkFromMyKey, true)
                    navController.navigate(BookOnDestination.ReadingMarathonLink.route)
                },
                onLogoutRequest = {
                    onLogout()
                    navController.navigateToLoginAndClearBackStack()
                },
            )
        }
        composable(BookOnDestination.Search.route) {
            BookOnSearchRoute(
                onBackClick = navController::navigateUp,
                onBookClick = { bookId -> navController.navigate(BookOnDestination.BookDetail.createRoute(bookId)) },
            )
        }
        composable(BookOnDestination.BookDetail.route, arguments = listOf(navArgument(BookOnDestination.BookDetail.bookIdArgument) { type = NavType.LongType })) { entry ->
            BookOnBookDetailRoute(
                bookId = entry.arguments?.getLong(BookOnDestination.BookDetail.bookIdArgument) ?: return@composable,
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
                onBookClick = { bookId -> navController.navigate(BookOnDestination.BookDetail.createRoute(bookId)) },
            )
        }
        composable(BookOnDestination.Favorites.route) {
            BookOnFavoriteBooksRoute(
                onBackClick = navController::navigateUp,
                onBookClick = { bookId -> navController.navigate(BookOnDestination.BookDetail.createRoute(bookId)) },
            )
        }
    }
}
