package com.teamnative.bookon.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.teamnative.bookon.feature.auth.presentation.signup.BookOnRegistrationViewModel
import com.teamnative.bookon.feature.auth.presentation.login.BookOnLoginRoute
import com.teamnative.bookon.feature.auth.presentation.passwordsetup.BookOnPasswordSetupRoute
import com.teamnative.bookon.feature.auth.presentation.passwordreset.BookOnPasswordResetEmailRoute
import com.teamnative.bookon.feature.auth.presentation.passwordreset.BookOnPasswordResetNewPasswordRoute
import com.teamnative.bookon.feature.auth.presentation.passwordreset.BookOnPasswordResetVerificationRoute
import com.teamnative.bookon.feature.auth.presentation.passwordreset.BookOnPasswordResetViewModel
import com.teamnative.bookon.feature.auth.presentation.readingmarathonlink.BookOnReadingMarathonLinkRoute
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
 * 여러 NavKey가 공유하는 Hilt ViewModel을 위한 [ViewModelStoreOwner]를 만든다.
 * Compose의 hiltViewModel()은 owner가 [HasDefaultViewModelProviderFactory]를 구현해야
 * Hilt 팩토리를 찾을 수 있고, ViewModel이 `SavedStateHandle`을 주입받으려면 그 팩토리가
 * 만드는 [CreationExtras]에 SavedStateRegistryOwner/ViewModelStoreOwner 정보가 있어야 한다.
 * 이 두 키를 만드는 클래스는 Kotlin `internal`이라 직접 접근할 수 없으므로, 현재 컴포지션의
 * 기본 owner(Activity)가 이미 올바르게 구성해 둔 [HasDefaultViewModelProviderFactory.defaultViewModelCreationExtras]를
 * 그대로 위임한다. ViewModel 인스턴스 자체는 우리 자신의 [ViewModelStore]에 저장되므로 공유
 * 범위에는 영향이 없다. 이 값을 반환한 Composable이 구성에서 사라지면 ViewModelStore를 clear한다.
 */
@Composable
private fun rememberFlowViewModelStoreOwner(): ViewModelStoreOwner {
    val defaultOwner = checkNotNull(LocalViewModelStoreOwner.current as? HasDefaultViewModelProviderFactory) {
        "LocalViewModelStoreOwner가 HasDefaultViewModelProviderFactory를 구현하지 않았다."
    }
    val defaultFactory = defaultOwner.defaultViewModelProviderFactory
    val defaultExtras = defaultOwner.defaultViewModelCreationExtras
    val owner = remember {
        object : ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
            override val viewModelStore = ViewModelStore()
            override val defaultViewModelProviderFactory: ViewModelProvider.Factory = defaultFactory
            override val defaultViewModelCreationExtras: CreationExtras = defaultExtras
        }
    }
    DisposableEffect(Unit) {
        onDispose { owner.viewModelStore.clear() }
    }
    return owner
}

/**
 * 앱의 최상위 Navigation 3 진입점이다.
 * 인증 여부에 따라 인증 플로우([BookOnAuthNavDisplay])와 메인 탭 플로우([BookOnMainNavDisplay]) 중
 * 하나만 구성(compose)한다. 로그인/로그아웃은 [isAuthenticated]를 직접 갈아끼우는 방식으로 처리하며,
 * 세션 상태가 바뀌면 인증/메인 Navigation 경계를 다시 만든다.
 */
@Composable
internal fun BookOnNavHost(
    isInitiallyAuthenticated: Boolean,
    pendingDeepLink: BookOnPendingDeepLink?,
    onLogout: () -> Unit,
    sessionEpoch: Long = 0L,
    onPendingDeepLinkConsumed: (Long) -> Unit = {},
) {
    // SessionViewModel이 토큰 만료를 감지해 인증 상태를 바꾸면 기존 메인 back stack이 남지 않도록
    // 인증 상태를 Composition key로 사용해 인증/메인 플로우와 그 하위 ViewModel을 함께 교체한다.
    key(isInitiallyAuthenticated, sessionEpoch) {
        if (isInitiallyAuthenticated) {
            BookOnMainNavDisplay(
                pendingDeepLink = pendingDeepLink,
                onPendingDeepLinkConsumed = onPendingDeepLinkConsumed,
                onLogout = onLogout,
            )
        } else {
            BookOnAuthNavDisplay(onAuthenticated = {})
        }
    }
}

/**
 * back stack을 시작 화면 하나만 남을 때까지 pop한다.
 * 다단계 플로우(비밀번호 재설정, 회원가입 등)가 완료된 뒤 첫 화면으로 복귀할 때 쓴다.
 * 플로우 단계 수가 늘어나도 이 함수를 쓰는 호출부는 고칠 필요가 없다.
 */
private fun <T : NavKey> NavBackStack<T>.popToStart() {
    while (size > 1) {
        removeLastOrNull()
    }
}

/** 로그인 전 화면들(로그인/회원가입/비밀번호 재설정)을 담당한다. 단일 back stack으로 충분하다. */
@Composable
private fun BookOnAuthNavDisplay(onAuthenticated: () -> Unit) {
    val backStack = rememberNavBackStack(BookOnDestination.Login)

    // 회원가입(Signup → PasswordSetup → VerificationCode) 3개 화면이 공유하는 ViewModel이다.
    // Activity 전체가 아니라 이 Composable(=인증 플로우)이 구성되어 있는 동안만 유지되도록
    // 직접 ViewModelStore를 만들고, 인증 플로우를 벗어나면(= 로그인 성공) clear한다.
    val registrationViewModel: BookOnRegistrationViewModel =
        hiltViewModel(viewModelStoreOwner = rememberFlowViewModelStoreOwner())

    // 비밀번호 재설정(이메일 → 인증코드 → 새 비밀번호) 3개 화면이 공유하는 ViewModel이다.
    // 이 흐름을 완전히 벗어나면(로그인 화면으로 복귀) clear한다.
    val passwordResetViewModel: BookOnPasswordResetViewModel =
        hiltViewModel(viewModelStoreOwner = rememberFlowViewModelStoreOwner())

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
                BookOnPasswordResetEmailRoute(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToVerification = { backStack.add(BookOnDestination.PasswordResetVerification) },
                    viewModel = passwordResetViewModel,
                )
            }
            entry<BookOnDestination.PasswordResetVerification> {
                BookOnPasswordResetVerificationRoute(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToNewPassword = { backStack.add(BookOnDestination.PasswordResetNewPassword) },
                    viewModel = passwordResetViewModel,
                )
            }
            entry<BookOnDestination.PasswordResetNewPassword> {
                BookOnPasswordResetNewPasswordRoute(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onResetCompleted = { backStack.popToStart() },
                    viewModel = passwordResetViewModel,
                )
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
                    initialProgressStep = null,
                    onBackClick = { backStack.removeLastOrNull() },
                    onNextClick = {
                        if (backStack.lastOrNull() == BookOnDestination.PasswordSetup) {
                            backStack.add(BookOnDestination.VerificationCode)
                        }
                    },
                    onEmailAlreadyUsed = {
                        if (backStack.lastOrNull() == BookOnDestination.PasswordSetup) {
                            backStack.removeLastOrNull()
                        }
                    },
                    viewModel = registrationViewModel,
                )
            }
            entry<BookOnDestination.VerificationCode> {
                BookOnVerificationCodeRoute(
                    initialProgressStep = null,
                    onBackClick = { backStack.removeLastOrNull() },
                    // Read365 연동은 로그인 후 My 화면에서만 제공하고, 회원가입은 즉시 완료한다.
                    onConfirmClick = {
                        if (backStack.lastOrNull() == BookOnDestination.VerificationCode) {
                        backStack.add(
                            BookOnDestination.SignupComplete(
                                isReadingMarathonLinked = false,
                            ),
                        )
                        }
                    },
                    viewModel = registrationViewModel,
                )
            }
            entry<BookOnDestination.SignupComplete> { key ->
                BookOnSignupCompleteRoute(
                    isReadingMarathonLinked = key.isReadingMarathonLinked,
                    // 회원가입 인증 API는 사용자 생성만 완료하고 access/refresh token을 발급하지 않는다.
                    // 토큰 없이 메인 화면으로 진입하면 인증 API가 401이 되므로 로그인 화면으로 돌아간다.
                    onStartClick = { backStack.popToStart() },
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
private fun BookOnMainNavDisplay(
    pendingDeepLink: BookOnPendingDeepLink?,
    onLogout: () -> Unit,
    onPendingDeepLinkConsumed: (Long) -> Unit,
) {
    val navigationState = rememberBookOnMainNavigationState(
        startRoute = BookOnDestination.Home,
        topLevelRoutes = mainDestinations,
    )
    val navigator = remember(navigationState) { BookOnMainNavigator(navigationState) }

    // 알림 탭 등으로 전달된 초기 목적지를 한 번만 push한다. token이 바뀔 때만(=새 알림 탭) 재실행된다.
    // LoanHistory는 My 탭 소속 화면이므로, push 전에 반드시 My 탭으로 전환해 뒤로가기 흐름을 자연스럽게 유지한다.
    LaunchedEffect(pendingDeepLink) {
        pendingDeepLink?.let {
            navigator.navigateToTab(BookOnDestination.My)
            navigator.push(it.destination)
            onPendingDeepLinkConsumed(it.token)
        }
    }

    // 비밀번호 재설정(이메일 → 인증코드 → 새 비밀번호) 3개 화면이 공유하는 ViewModel이다.
    // 인증 전 흐름과는 별개의 인스턴스이며, 메인 플로우가 사라지면(로그아웃) clear한다.
    val passwordResetViewModel: BookOnPasswordResetViewModel =
        hiltViewModel(viewModelStoreOwner = rememberFlowViewModelStoreOwner())

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
                        onBookClick = { bookId -> navigator.push(BookOnDestination.BookDetail(bookId)) },
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
                    BookOnPasswordResetEmailRoute(
                        onNavigateBack = navigator::goBack,
                        onNavigateToVerification = { navigator.push(BookOnDestination.PasswordResetVerification) },
                        viewModel = passwordResetViewModel,
                    )
                }
                entry<BookOnDestination.PasswordResetVerification> {
                    BookOnPasswordResetVerificationRoute(
                        onNavigateBack = navigator::goBack,
                        onNavigateToNewPassword = { navigator.push(BookOnDestination.PasswordResetNewPassword) },
                        viewModel = passwordResetViewModel,
                    )
                }
                entry<BookOnDestination.PasswordResetNewPassword> {
                    BookOnPasswordResetNewPasswordRoute(
                        onNavigateBack = navigator::goBack,
                        onResetCompleted = navigator::popToTabRoot,
                        viewModel = passwordResetViewModel,
                    )
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
