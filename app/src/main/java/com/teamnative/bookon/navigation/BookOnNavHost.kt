package com.teamnative.bookon.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
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

private const val PasswordSetupStep = 2

/**
 * 앱의 최상위 Navigation 3 진입점이다.
 * 인증 여부에 따라 인증 플로우([BookOnAuthNavDisplay])와 메인 탭 플로우([BookOnMainNavDisplay]) 중
 * 하나만 구성(compose)한다. 로그인/로그아웃은 [isAuthenticated]를 직접 갈아끼우는 방식으로 처리하며,
 * 세션 상태가 바뀔 때마다 NavHost 전체를 다시 만들지 않는다.
 */
@Composable
fun BookOnNavHost(isInitiallyAuthenticated: Boolean, onLogout: () -> Unit) {
    var isAuthenticated by rememberSaveable { mutableStateOf(isInitiallyAuthenticated) }

    if (isAuthenticated) {
        BookOnMainNavDisplay(
            onLogout = {
                onLogout()
                isAuthenticated = false
            },
        )
    } else {
        BookOnAuthNavDisplay(onAuthenticated = { isAuthenticated = true })
    }
}

/** 로그인 전 화면들(로그인/회원가입/비밀번호 재설정)을 담당한다. 단일 back stack으로 충분하다. */
@Composable
private fun BookOnAuthNavDisplay(onAuthenticated: () -> Unit) {
    val backStack = rememberNavBackStack(BookOnDestination.Login)

    // 회원가입(Signup → PasswordSetup → VerificationCode) 3개 화면이 공유하는 ViewModel이다.
    // Activity 전체가 아니라 이 Composable(=인증 플로우)이 구성되어 있는 동안만 유지되도록
    // 직접 ViewModelStore를 만들고, 인증 플로우를 벗어나면(= 로그인 성공) clear한다.
    val registrationViewModelStoreOwner = remember {
        object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
        }
    }
    DisposableEffect(Unit) {
        onDispose { registrationViewModelStoreOwner.viewModelStore.clear() }
    }
    val registrationViewModel: BookOnRegistrationViewModel =
        hiltViewModel(viewModelStoreOwner = registrationViewModelStoreOwner)

    // Navigation 2의 savedStateHandle 결과 전달을 대체하는 회원가입 플로우 전용 상태이다.
    var progressStepOverride by remember { mutableIntStateOf(-1) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        // 인증 플로우의 push/pop 화면 전환은 애니메이션 없이 즉시 표시한다.
        // predictive back(엣지 뒤로가기 제스처) 진행 중 피드백은 기본 동작을 유지한다.
        transitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        popTransitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        entryProvider = entryProvider {
            entry<BookOnDestination.Login> {
                BookOnLoginRoute(
                    onLoginClick = onAuthenticated,
                    onSignupClick = { backStack.add(BookOnDestination.Signup) },
                    onForgotPasswordClick = { backStack.add(BookOnDestination.PasswordReset) },
                )
            }
            entry<BookOnDestination.PasswordReset> {
                BookOnPasswordResetRoute(onNavigateBack = { backStack.removeLastOrNull() })
            }
            entry<BookOnDestination.Signup> {
                BookOnSignupRoute(
                    onBackClick = { backStack.removeLastOrNull() },
                    onNextClick = { backStack.add(BookOnDestination.PasswordSetup) },
                    viewModel = registrationViewModel,
                )
            }
            entry<BookOnDestination.PasswordSetup> {
                BookOnPasswordSetupRoute(
                    initialProgressStep = progressStepOverride.takeIf { it >= 0 },
                    onBackClick = { backStack.removeLastOrNull() },
                    onNextClick = { backStack.add(BookOnDestination.VerificationCode) },
                    viewModel = registrationViewModel,
                )
            }
            entry<BookOnDestination.VerificationCode> {
                BookOnVerificationCodeRoute(
                    initialProgressStep = progressStepOverride.takeIf { it >= 0 },
                    onBackClick = { backStack.removeLastOrNull() },
                    onConfirmClick = { backStack.add(BookOnDestination.ReadingMarathonSignup) },
                    viewModel = registrationViewModel,
                )
            }
            entry<BookOnDestination.ReadingMarathonSignup> {
                BookOnReadingMarathonSignupRoute(
                    onBackClick = {
                        progressStepOverride = PasswordSetupStep
                        backStack.removeLastOrNull()
                    },
                    onUseClick = { backStack.add(BookOnDestination.ReadingMarathonLink(openedFromMy = false)) },
                    onSkipClick = {
                        backStack.add(BookOnDestination.SignupComplete(isReadingMarathonLinked = false))
                    },
                )
            }
            entry<BookOnDestination.ReadingMarathonLink> { key ->
                BookOnReadingMarathonLinkRoute(
                    onBackClick = { backStack.removeLastOrNull() },
                    onSkipClick = {
                        if (key.openedFromMy) {
                            backStack.removeLastOrNull()
                        } else {
                            backStack.add(BookOnDestination.SignupComplete(isReadingMarathonLinked = false))
                        }
                    },
                    onCompleteClick = {
                        if (key.openedFromMy) {
                            backStack.removeLastOrNull()
                        } else {
                            backStack.add(BookOnDestination.SignupComplete(isReadingMarathonLinked = true))
                        }
                    },
                )
            }
            entry<BookOnDestination.SignupComplete> { key ->
                BookOnSignupCompleteRoute(
                    isReadingMarathonLinked = key.isReadingMarathonLinked,
                    onStartClick = onAuthenticated,
                )
            }
        },
    )
}

/**
 * 로그인 이후 메인 탭 화면들을 담당한다.
 * 하단 탭(Home/Ranking/Library/My)마다 독립된 back stack을 유지하는 멀티 백스택 구조이다.
 */
@Composable
private fun BookOnMainNavDisplay(onLogout: () -> Unit) {
    val navigationState = rememberBookOnMainNavigationState(
        startRoute = BookOnDestination.Home,
        topLevelRoutes = mainDestinations,
    )
    val navigator = remember(navigationState) { BookOnMainNavigator(navigationState) }

    val bottomBar: @Composable () -> Unit = {
        BookOnMainBottomBar(
            current = navigationState.topLevelRoute,
            onDestinationSelected = navigator::navigateToTab,
        )
    }

    NavDisplay(
        entries = navigationState.toEntries(
            entryProvider {
                entry<BookOnDestination.Home> {
                    BookOnHomeRoute(
                        bottomBar = bottomBar,
                        onSearchClick = { navigator.push(BookOnDestination.Search) },
                        onNewBooksClick = { navigator.push(BookOnDestination.NewBooks) },
                        onNotificationClick = {
                            // TODO: 서버 알림 API 또는 알림 화면이 준비되면 이 이벤트에 연결한다.
                        },
                    )
                }
                entry<BookOnDestination.Ranking> {
                    BookOnRankingRoute(bottomBar = bottomBar)
                }
                entry<BookOnDestination.Library> {
                    BookOnLibraryRoute(
                        bottomBar = bottomBar,
                        onBookClick = { bookId -> navigator.push(BookOnDestination.BookDetail(bookId)) },
                    )
                }
                entry<BookOnDestination.My> {
                    BookOnMyRoute(
                        bottomBar = bottomBar,
                        onPasswordChangeClick = { navigator.push(BookOnDestination.PasswordReset) },
                        onLoanHistoryClick = { navigator.push(BookOnDestination.LoanHistory) },
                        onFavoriteClick = { navigator.push(BookOnDestination.Favorites) },
                        onReadingMarathonLinkClick = {
                            navigator.push(BookOnDestination.ReadingMarathonLink(openedFromMy = true))
                        },
                        onLogoutRequest = onLogout,
                    )
                }
                entry<BookOnDestination.Search> {
                    BookOnSearchRoute(
                        onBackClick = navigator::goBack,
                        onBookClick = { bookId -> navigator.push(BookOnDestination.BookDetail(bookId)) },
                    )
                }
                entry<BookOnDestination.BookDetail> { key ->
                    BookOnBookDetailRoute(
                        bookId = key.bookId,
                        onBackClick = navigator::goBack,
                    )
                }
                entry<BookOnDestination.NewBooks> {
                    BookOnNewBooksRoute(onBackClick = navigator::goBack)
                }
                entry<BookOnDestination.LoanHistory> {
                    BookOnLoanHistoryRoute(
                        onBackClick = navigator::goBack,
                        onBookClick = { bookId -> navigator.push(BookOnDestination.BookDetail(bookId)) },
                    )
                }
                entry<BookOnDestination.Favorites> {
                    BookOnFavoriteBooksRoute(
                        onBackClick = navigator::goBack,
                        onBookClick = { bookId -> navigator.push(BookOnDestination.BookDetail(bookId)) },
                    )
                }
                entry<BookOnDestination.PasswordReset> {
                    BookOnPasswordResetRoute(onNavigateBack = navigator::goBack)
                }
                entry<BookOnDestination.ReadingMarathonLink> {
                    BookOnReadingMarathonLinkRoute(
                        onBackClick = navigator::goBack,
                        onSkipClick = navigator::goBack,
                        onCompleteClick = navigator::goBack,
                    )
                }
            },
        ),
        onBack = navigator::goBack,
        // 메인 플로우의 push/pop 화면 전환은 애니메이션 없이 즉시 처리한다.
        // predictive back(엣지 뒤로가기 제스처) 진행 중 피드백은 기본 동작을 유지한다.
        transitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        popTransitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
    )
}
