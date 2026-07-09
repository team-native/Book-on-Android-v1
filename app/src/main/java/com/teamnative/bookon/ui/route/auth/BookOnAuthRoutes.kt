package com.teamnative.bookon.ui.route.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.ui.screen.auth.BookOnLoginScreen
import com.teamnative.bookon.ui.screen.auth.BookOnPasswordSetupScreen
import com.teamnative.bookon.ui.screen.auth.BookOnPrivacyPolicyScreen
import com.teamnative.bookon.ui.screen.auth.BookOnReadingMarathonLinkScreen
import com.teamnative.bookon.ui.screen.auth.BookOnReadingMarathonSignupScreen
import com.teamnative.bookon.ui.screen.auth.BookOnSignupCompleteScreen
import com.teamnative.bookon.ui.screen.auth.BookOnSignupScreen
import com.teamnative.bookon.ui.screen.auth.BookOnVerificationCodeScreen
import com.teamnative.bookon.ui.screen.auth.previewLoginUiState
import com.teamnative.bookon.ui.screen.auth.previewPasswordSetupUiState
import com.teamnative.bookon.ui.screen.auth.previewPrivacyPolicyUiState
import com.teamnative.bookon.ui.screen.auth.previewReadingMarathonLinkUiState
import com.teamnative.bookon.ui.screen.auth.previewReadingMarathonSignupUiState
import com.teamnative.bookon.ui.screen.auth.previewSignupCompleteUiState
import com.teamnative.bookon.ui.screen.auth.previewSignupUiState
import com.teamnative.bookon.ui.screen.auth.previewVerificationCodeUiState
import com.teamnative.bookon.uiState.BookOnPasswordFieldUiState
import com.teamnative.bookon.uiState.BookOnTextFieldUiState

/**
 * 로그인 Route는 서버 인증 전 샘플 입력 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnLoginRoute(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val uiState = previewLoginUiState().copy(
        email = BookOnTextFieldUiState(
            value = email,
            placeholder = stringResource(R.string.email_address),
            suffixText = stringResource(R.string.email_domain_gsm),
        ),
        password = BookOnPasswordFieldUiState(
            value = password,
            placeholder = stringResource(R.string.password),
        ),
    )

    BookOnLoginScreen(
        uiState = uiState,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = onLoginClick,
        onSignupClick = onSignupClick,
        onForgotPasswordClick = {},
    )
}

/**
 * 회원가입 Route는 학교 정보 입력 단계의 샘플 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnSignupRoute(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    BookOnSignupScreen(
        uiState = previewSignupUiState(),
        onBackClick = onBackClick,
        onPasswordNoticeClick = {},
        onEmailChange = {},
        onNameChange = {},
        onGenderSelected = {},
        onDepartmentClick = {},
        onNextClick = onNextClick,
    )
}

/**
 * 인증번호 Route는 서버 인증 전 샘플 코드 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnVerificationCodeRoute(onBackClick: () -> Unit, onConfirmClick: () -> Unit) {
    BookOnVerificationCodeScreen(
        uiState = previewVerificationCodeUiState(),
        onBackClick = onBackClick,
        onCodeChange = {},
        onResendClick = {},
        onConfirmClick = onConfirmClick,
    )
}

/**
 * 비밀번호 설정 Route는 샘플 비밀번호 입력 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnPasswordSetupRoute(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var privacyChecked by rememberSaveable { mutableStateOf(false) }
    val uiState = previewPasswordSetupUiState().copy(
        password = BookOnPasswordFieldUiState(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
        ),
        passwordConfirm = BookOnPasswordFieldUiState(
            value = passwordConfirm,
            label = stringResource(R.string.password_confirm_short),
            placeholder = stringResource(R.string.password_confirm),
        ),
        privacyChecked = privacyChecked,
    )

    BookOnPasswordSetupScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPasswordNoticeClick = {},
        onPasswordChange = { password = it },
        onPasswordConfirmChange = { passwordConfirm = it },
        onPrivacyCheckedChange = { privacyChecked = it },
        onNextClick = onNextClick,
    )
}

/**
 * 개인정보 안내 Route는 약관 섹션 샘플 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnPrivacyPolicyRoute(onBackClick: () -> Unit) {
    BookOnPrivacyPolicyScreen(
        uiState = previewPrivacyPolicyUiState(),
        onBackClick = onBackClick,
    )
}

/**
 * 독서마라톤 안내 Route는 연동 안내 샘플 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnReadingMarathonSignupRoute(
    onBackClick: () -> Unit,
    onUseClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    BookOnReadingMarathonSignupScreen(
        uiState = previewReadingMarathonSignupUiState(),
        onBackClick = onBackClick,
        onUseClick = onUseClick,
        onSkipClick = onSkipClick,
    )
}

/**
 * 독서마라톤 계정 연동 Route는 샘플 계정 입력 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnReadingMarathonLinkRoute(
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    var marathonId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var agreementChecked by rememberSaveable { mutableStateOf(false) }
    val uiState = previewReadingMarathonLinkUiState().copy(
        marathonId = BookOnTextFieldUiState(
            value = marathonId,
            label = stringResource(R.string.reading_marathon_id),
            placeholder = stringResource(R.string.reading_marathon_id),
        ),
        password = BookOnPasswordFieldUiState(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
        ),
        agreement = previewReadingMarathonLinkUiState().agreement.copy(checked = agreementChecked),
    )

    BookOnReadingMarathonLinkScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onIdChange = { marathonId = it },
        onPasswordChange = { password = it },
        onAgreementChange = { agreementChecked = it },
        onOauthClick = {},
        onSkipClick = onSkipClick,
        onCompleteClick = onCompleteClick,
    )
}

/**
 * 가입 완료 Route는 샘플 가입 완료 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnSignupCompleteRoute(onStartClick: () -> Unit) {
    BookOnSignupCompleteScreen(
        uiState = previewSignupCompleteUiState(),
        onStartClick = onStartClick,
    )
}
