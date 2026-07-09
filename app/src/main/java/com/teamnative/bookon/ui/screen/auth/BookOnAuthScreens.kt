package com.teamnative.bookon.ui.screen.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.ui.Component.auth.BookOnAuthFormScaffold
import com.teamnative.bookon.ui.Component.auth.BookOnDropdownField
import com.teamnative.bookon.ui.Component.auth.BookOnGenderSelector
import com.teamnative.bookon.ui.Component.auth.BookOnSignupCompleteSummary
import com.teamnative.bookon.ui.Component.auth.BookOnSignupStepHeader
import com.teamnative.bookon.ui.Component.auth.BookOnSkipTextButton
import com.teamnative.bookon.ui.Component.auth.BookOnVerificationCodeField
import com.teamnative.bookon.ui.Component.auth.BookOnVerificationStatus
import com.teamnative.bookon.ui.Component.bar.BookOnStepProgress
import com.teamnative.bookon.ui.Component.bar.BookOnTopBar
import com.teamnative.bookon.ui.Component.button.BookOnPrimaryButton
import com.teamnative.bookon.ui.Component.card.BookOnInfoCard
import com.teamnative.bookon.ui.Component.marathon.BookOnMarathonAgreementRow
import com.teamnative.bookon.ui.Component.marathon.BookOnMarathonLinkCard
import com.teamnative.bookon.ui.Component.textfield.BookOnPasswordField
import com.teamnative.bookon.ui.Component.textfield.BookOnTextField
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.BookOnPasswordFieldUiState
import com.teamnative.bookon.uiState.BookOnTextFieldUiState
import com.teamnative.bookon.uiState.auth.BookOnDropdownFieldUiState
import com.teamnative.bookon.uiState.auth.BookOnLoginUiState
import com.teamnative.bookon.uiState.auth.BookOnOptionButtonUiState
import com.teamnative.bookon.uiState.auth.BookOnPasswordSetupUiState
import com.teamnative.bookon.uiState.auth.BookOnPrivacyPolicySectionUiState
import com.teamnative.bookon.uiState.auth.BookOnPrivacyPolicyUiState
import com.teamnative.bookon.uiState.auth.BookOnReadingMarathonLinkUiState
import com.teamnative.bookon.uiState.auth.BookOnReadingMarathonSignupUiState
import com.teamnative.bookon.uiState.auth.BookOnSignupCompleteUiState
import com.teamnative.bookon.uiState.auth.BookOnSignupUiState
import com.teamnative.bookon.uiState.auth.BookOnVerificationCodeUiState
import com.teamnative.bookon.uiState.marathon.BookOnMarathonAgreementUiState

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
 * 로그인 화면은 이메일, 비밀번호, 로그인 CTA, 회원가입 진입을 표시한다.
 */
@Composable
fun BookOnLoginScreen(
    uiState: BookOnLoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.ScreenHorizontal)
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.Section * 4))
            Text(
                text = uiState.title,
                style = BookOnTypography.screenTitle,
                color = BookOnColor.TextPrimary,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
            BookOnTextField(
                uiState = uiState.email,
                onValueChange = onEmailChange,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Content))

            BookOnPasswordField(
                uiState = uiState.password,
                onValueChange = onPasswordChange,
            )

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = AppSpacing.Small)
                    .clickable(role = Role.Button, onClick = onForgotPasswordClick),
                text = stringResource(R.string.forgot_password),
                style = BookOnTypography.caption,
                color = BookOnColor.TextSecondary,
            )
            Spacer(modifier = Modifier.weight(1f))

            BookOnPrimaryButton(
                text = stringResource(R.string.login),
                onClick = onLoginClick,
                enabled = uiState.loginEnabled,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Content))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(role = Role.Button, onClick = onSignupClick),
                text = stringResource(R.string.go_to_signup),
                style = BookOnTypography.caption,
                color = BookOnColor.PrimaryPressed,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
        }
    }
}

/**
 * 회원가입 Route는 학교 정보 입력 단계의 샘플 상태를 Screen에 전달한다.
 */
@Composable
fun BookOnSignupRoute(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    BookOnSignupScreen(
        uiState = previewSignupUiState(),
        onBackClick = onBackClick,
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
            label = stringResource(R.string.password_confirm),
            placeholder = stringResource(R.string.password_confirm),
        ),
        privacyChecked = privacyChecked,
    )

    BookOnPasswordSetupScreen(
        uiState = uiState,
        onBackClick = onBackClick,
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

/**
 * 회원가입 화면은 학교 이메일, 이름, 성별, 학과 입력 컴포넌트를 조립한다.
 */
@Composable
fun BookOnSignupScreen(
    uiState: BookOnSignupUiState,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onGenderSelected: (Int) -> Unit,
    onDepartmentClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_next),
                onClick = onNextClick,
                enabled = uiState.nextEnabled,
            )
        },
    ) {
        BookOnSignupStepHeader(step = 1, title = uiState.title, description = uiState.description)
        BookOnTextField(uiState = uiState.email, onValueChange = onEmailChange)
        BookOnTextField(uiState = uiState.name, onValueChange = onNameChange)
        BookOnGenderSelector(
            options = uiState.genderOptions,
            onGenderSelected = onGenderSelected,
        )
        BookOnDropdownField(
            uiState = uiState.department,
            onClick = onDepartmentClick,
        )
    }
}

/**
 * 인증번호 화면은 6자리 코드 입력과 만료/재전송 상태를 표시한다.
 */
@Composable
fun BookOnVerificationCodeScreen(
    uiState: BookOnVerificationCodeUiState,
    onBackClick: () -> Unit,
    onCodeChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_verify_continue),
                onClick = onConfirmClick,
                enabled = uiState.confirmEnabled,
            )
        },
    ) {
        BookOnSignupStepHeader(step = 1, title = uiState.title, description = uiState.description)
        BookOnVerificationCodeField(
            code = uiState.code,
            onCodeChange = onCodeChange,
            isError = uiState.errorText != null,
        )
        BookOnVerificationStatus(
            expireText = uiState.expireText,
            resendText = stringResource(R.string.verification_code_resend),
            onResendClick = onResendClick,
            errorText = uiState.errorText,
        )
    }
}

/**
 * 비밀번호 설정 화면은 비밀번호 입력과 개인정보 동의 상태를 표시한다.
 */
@Composable
fun BookOnPasswordSetupScreen(
    uiState: BookOnPasswordSetupUiState,
    onBackClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onPrivacyCheckedChange: (Boolean) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_next),
                onClick = onNextClick,
                enabled = uiState.nextEnabled,
            )
        },
    ) {
        BookOnSignupStepHeader(step = 2, title = uiState.title, description = uiState.description)
        BookOnPasswordField(uiState = uiState.password, onValueChange = onPasswordChange)
        BookOnPasswordField(uiState = uiState.passwordConfirm, onValueChange = onPasswordConfirmChange)
        BookOnInfoCard(
            title = uiState.ruleTitle,
            description = uiState.ruleText,
        )
        BookOnMarathonAgreementRow(
            text = stringResource(R.string.privacy_required_agreement),
            checked = uiState.privacyChecked,
            onCheckedChange = onPrivacyCheckedChange,
        )
    }
}

/**
 * 개인정보 안내 화면은 긴 약관 내용을 스크롤 가능한 카드 목록으로 표시한다.
 */
@Composable
fun BookOnPrivacyPolicyScreen(
    uiState: BookOnPrivacyPolicyUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            BookOnTopBar(
                title = uiState.title,
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            item { BookOnStepProgress(currentStep = 2, totalStep = 3) }
            uiState.sections.forEach { section ->
                item {
                    BookOnInfoCard(
                        title = section.title,
                        description = section.body,
                    )
                }
            }
            item {
                Text(
                    text = uiState.notice,
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextSecondary,
                )
            }
        }
    }
}

/**
 * 독서마라톤 안내 화면은 연동 이점과 건너뛰기/다음 액션을 표시한다.
 */
@Composable
fun BookOnReadingMarathonSignupScreen(
    uiState: BookOnReadingMarathonSignupUiState,
    onBackClick: () -> Unit,
    onUseClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_next),
                onClick = onUseClick,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Item))
            BookOnSkipTextButton(
                text = stringResource(R.string.skip_reading_marathon),
                onClick = onSkipClick,
            )
        },
    ) {
        BookOnSignupStepHeader(step = 3, title = uiState.title, description = uiState.description)
        BookOnMarathonLinkCard(
            title = uiState.useTitle,
            description = uiState.useDescription,
            onClick = onUseClick,
        )
        BookOnInfoCard(
            title = stringResource(R.string.reading_marathon),
            description = uiState.benefitText,
        )
        uiState.laterNotice?.let { notice ->
            Text(
                text = notice,
                style = BookOnTypography.caption,
                color = BookOnColor.TextSecondary,
            )
        }
    }
}

/**
 * 독서마라톤 계정 연동 화면은 아이디, 비밀번호, 개인정보 제공 동의를 받는다.
 */
@Composable
fun BookOnReadingMarathonLinkScreen(
    uiState: BookOnReadingMarathonLinkUiState,
    onBackClick: () -> Unit,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAgreementChange: (Boolean) -> Unit,
    onSkipClick: () -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = { BookOnTopBar(title = "", onBackClick = onBackClick) },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.complete_signup_with_link),
                onClick = onCompleteClick,
                enabled = uiState.linkEnabled,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Item))
            BookOnSkipTextButton(
                text = stringResource(R.string.skip_reading_marathon),
                onClick = onSkipClick,
            )
        },
    ) {
        BookOnSignupStepHeader(step = 3, title = uiState.title, description = uiState.description)
        BookOnTextField(uiState = uiState.marathonId, onValueChange = onIdChange)
        BookOnPasswordField(uiState = uiState.password, onValueChange = onPasswordChange)
        BookOnMarathonAgreementRow(
            uiState = uiState.agreement,
            onCheckedChange = onAgreementChange,
        )
    }
}

/**
 * 가입 완료 화면은 서버 사용자 요약이 들어올 위치를 샘플 상태로 표시한다.
 */
@Composable
fun BookOnSignupCompleteScreen(
    uiState: BookOnSignupCompleteUiState,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(AppSpacing.ScreenHorizontal)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = uiState.title,
                style = BookOnTypography.screenTitle,
                color = BookOnColor.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Content))
            Text(
                text = uiState.message,
                style = BookOnTypography.bodyMedium,
                color = BookOnColor.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Section))
            BookOnSignupCompleteSummary(
                ownedBookCountText = uiState.ownedBookCountText,
                ownedBookDescription = stringResource(R.string.owned_books),
                marathonStatusText = uiState.marathonStatusText,
                marathonDescription = stringResource(R.string.reading_marathon),
            )
            Spacer(modifier = Modifier.weight(1f))
            BookOnPrimaryButton(
                text = stringResource(R.string.action_start),
                onClick = onStartClick,
            )
        }
    }
}

private fun previewLoginUiState() = BookOnLoginUiState(
    title = "환영합니다!",
    email = BookOnTextFieldUiState("", placeholder = "이메일 주소"),
    password = BookOnPasswordFieldUiState("", placeholder = "비밀번호"),
)

private fun previewSignupUiState() = BookOnSignupUiState(
    stepText = "STEP 1 / 3",
    title = "학교 정보",
    description = "학교 계정으로 간편하게 가입하세요!",
    email = BookOnTextFieldUiState("s20000", label = "학교 이메일", placeholder = "이메일 주소"),
    name = BookOnTextFieldUiState("홍길동", label = "이름", placeholder = "이름"),
    genderOptions = listOf(
        BookOnOptionButtonUiState("남자", true),
        BookOnOptionButtonUiState("여자", false),
    ),
    department = BookOnDropdownFieldUiState("AI과", "학과"),
)

private fun previewVerificationCodeUiState() = BookOnVerificationCodeUiState(
    stepText = "STEP 1 / 3",
    title = "인증번호 입력",
    description = "s20000@gsm.hs.kr 으로 보낸\n6자리 코드를 입력해 주세요",
    code = "2222",
    expireText = "04:52 후 만료",
)

private fun previewPasswordSetupUiState() = BookOnPasswordSetupUiState(
    stepText = "STEP 2 / 3",
    title = "계정 정보",
    description = "비밀번호를 설정해주세요",
    password = BookOnPasswordFieldUiState("", label = "비밀번호", placeholder = "비밀번호"),
    passwordConfirm = BookOnPasswordFieldUiState("", label = "비밀번호 확인", placeholder = "비밀번호 확인"),
    ruleTitle = "비밀번호 유의사항",
    ruleText = "영문(대·소문자), 숫자\n특수문자를 포함한 6~15자를 입력",
    privacyChecked = false,
)

private fun previewPrivacyPolicyUiState() = BookOnPrivacyPolicyUiState(
    stepText = "STEP 2 / 3",
    title = "개인정보 수집 및 이용 안내",
    sections = listOf(
        BookOnPrivacyPolicySectionUiState("1. 수집 항목", "이름, 이메일 주소, 비밀번호"),
        BookOnPrivacyPolicySectionUiState("2. 수집 목적", "회원 식별 및 관리\n서비스 제공 및 공지사항 전달"),
        BookOnPrivacyPolicySectionUiState("3. 보유 및 이용 기간", "회원 탈퇴 시까지"),
    ),
    notice = "동의를 거부할 권리가 있으며, 동의를 거부할 경우 회원가입이 제한될 수 있습니다.",
)

private fun previewReadingMarathonSignupUiState() = BookOnReadingMarathonSignupUiState(
    stepText = "STEP 3 / 3",
    title = "독서마라톤",
    description = "독서마라톤 계정을 연동하면\n읽은 책이 자동으로 기록돼요",
    useTitle = "독서마라톤 이용하기",
    useDescription = "교내 독서마라톤에 참여 중이라면 연동하세요",
    benefitText = "연동하면 홈 · 마이페이지에서 진척도와 랭킹이 자동으로 표시돼요.",
    laterNotice = "지금 연동하지 않아도 괜찮아요.\n마이페이지에서 언제든 다시 연동할 수 있어요!",
)

private fun previewReadingMarathonLinkUiState() = BookOnReadingMarathonLinkUiState(
    stepText = "STEP 3 / 3",
    title = "계정연동",
    description = "독서마라톤 아이디와 비밀번호를 입력해 주세요",
    marathonId = BookOnTextFieldUiState("", label = "독서마라톤 아이디", placeholder = "독서마라톤 아이디"),
    password = BookOnPasswordFieldUiState("", label = "비밀번호", placeholder = "비밀번호"),
    agreement = BookOnMarathonAgreementUiState(
        text = "독서마라톤 계정 연동을 위한 개인정보 제3자 제공에 동의합니다.",
        checked = false,
    ),
)

private fun previewSignupCompleteUiState() = BookOnSignupCompleteUiState(
    title = "가입 완료!",
    message = "홍길동 님, 환영해요.\n이제 Book - on에서 마음껏 읽어보세요.",
    ownedBookCountText = "4,218",
    marathonStatusText = "연동됨",
)

@Preview(showBackground = true)
@Composable
private fun BookOnLoginScreenPreview() {
    BookOnTheme {
        BookOnLoginScreen(
            uiState = previewLoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignupClick = {},
            onForgotPasswordClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSignupScreenPreview() {
    BookOnTheme {
        BookOnSignupScreen(
            uiState = previewSignupUiState(),
            onBackClick = {},
            onEmailChange = {},
            onNameChange = {},
            onGenderSelected = {},
            onDepartmentClick = {},
            onNextClick = {},
        )
    }
}
