package com.teamnative.bookon.navigation

internal sealed interface BookOnDestination {
    val route: String

    data object Login : BookOnDestination { override val route = "login" }
    data object Signup : BookOnDestination { override val route = "signup" }
    data object VerificationCode : BookOnDestination { override val route = "verificationCode" }
    data object PasswordSetup : BookOnDestination { override val route = "passwordSetup" }
    data object PasswordReset : BookOnDestination { override val route = "passwordReset" }
    data object ReadingMarathonSignup : BookOnDestination { override val route = "readingMarathonSignup" }
    data object ReadingMarathonLink : BookOnDestination { override val route = "readingMarathonLink" }
    data object SignupComplete : BookOnDestination {
        const val isReadingMarathonLinkedArgument = "isReadingMarathonLinked"
        override val route = "signupComplete/{$isReadingMarathonLinkedArgument}"

        /** 가입 완료 화면으로 독서마라톤 실제 연동 결과를 전달하는 경로를 만든다. */
        fun createRoute(isReadingMarathonLinked: Boolean): String =
            "signupComplete/$isReadingMarathonLinked"
    }
    data object Home : BookOnDestination { override val route = "home" }
    data object Ranking : BookOnDestination { override val route = "ranking" }
    data object Library : BookOnDestination { override val route = "library" }
    data object My : BookOnDestination { override val route = "my" }
    data object Search : BookOnDestination { override val route = "search" }
    data object BookDetail : BookOnDestination {
        const val bookIdArgument = "bookId"
        override val route = "bookDetail/{$bookIdArgument}"
        fun createRoute(bookId: Long) = "bookDetail/$bookId"
    }
    data object NewBooks : BookOnDestination { override val route = "newBooks" }
    data object LoanHistory : BookOnDestination { override val route = "loanHistory" }
    data object Favorites : BookOnDestination { override val route = "favorites" }
}

internal val mainDestinations = listOf(
    BookOnDestination.Home,
    BookOnDestination.Ranking,
    BookOnDestination.Library,
    BookOnDestination.My,
)
