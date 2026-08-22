package com.teamnative.bookon.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 앱의 모든 화면을 나타내는 Navigation 3 back stack 키이다.
 * [Serializable]이므로 프로세스 데스 이후에도 back stack이 복원될 수 있다.
 */
@Serializable
internal sealed interface BookOnDestination : NavKey {
    @Serializable data object Login : BookOnDestination
    @Serializable data object Signup : BookOnDestination
    @Serializable data object VerificationCode : BookOnDestination
    @Serializable data object PasswordSetup : BookOnDestination

    /** 비밀번호 재설정 1단계(이메일 입력)이다. */
    @Serializable data object PasswordReset : BookOnDestination

    /** 비밀번호 재설정 2단계(인증코드 확인)이다. */
    @Serializable data object PasswordResetVerification : BookOnDestination

    /** 비밀번호 재설정 3단계(새 비밀번호 설정)이다. */
    @Serializable data object PasswordResetNewPassword : BookOnDestination

    @Serializable data object ReadingMarathonSignup : BookOnDestination

    /** [openedFromMy]가 true이면 마이페이지에서 진입한 것이므로 완료/건너뛰기 시 가입 완료 화면 대신 뒤로 돌아간다. */
    @Serializable data class ReadingMarathonLink(val openedFromMy: Boolean = false) : BookOnDestination

    @Serializable data class SignupComplete(val isReadingMarathonLinked: Boolean) : BookOnDestination

    @Serializable data object Home : BookOnDestination
    @Serializable data object Ranking : BookOnDestination
    @Serializable data object Library : BookOnDestination
    @Serializable data object My : BookOnDestination

    @Serializable data object Search : BookOnDestination
    @Serializable data class BookDetail(val bookId: Long) : BookOnDestination
    @Serializable data object NewBooks : BookOnDestination
    @Serializable data object LoanHistory : BookOnDestination
    @Serializable data object Favorites : BookOnDestination
}

/** 하단 탭에 표시되는 최상위 목적지 4개를 기본 순서대로 제공한다. */
internal val mainDestinations: List<BookOnDestination> = listOf(
    BookOnDestination.Home,
    BookOnDestination.Ranking,
    BookOnDestination.Library,
    BookOnDestination.My,
)
