package com.teamnative.bookon.ui.screen.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
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

@Composable
internal fun previewLoginUiState() = BookOnLoginUiState(
    title = stringResource(R.string.login_title),
    email = BookOnTextFieldUiState(
        value = "",
        placeholder = stringResource(R.string.email_address),
        suffixText = stringResource(R.string.email_domain_gsm),
    ),
    password = BookOnPasswordFieldUiState("", placeholder = stringResource(R.string.password)),
)

@Composable
internal fun previewSignupUiState() = BookOnSignupUiState(
    stepText = stringResource(R.string.signup_step_1),
    title = stringResource(R.string.signup_school_info_title),
    description = stringResource(R.string.signup_school_account_description),
    email = BookOnTextFieldUiState(
        value = "",
        label = stringResource(R.string.school_email),
        placeholder = stringResource(R.string.email_address),
        suffixText = stringResource(R.string.email_domain_gsm),
    ),
    name = BookOnTextFieldUiState("", label = stringResource(R.string.name), placeholder = stringResource(R.string.name)),
    genderOptions = listOf(
        BookOnOptionButtonUiState(stringResource(R.string.male), false),
        BookOnOptionButtonUiState(stringResource(R.string.female), false),
    ),
    department = BookOnDropdownFieldUiState("", stringResource(R.string.department)),
)

@Composable
internal fun previewVerificationCodeUiState() = BookOnVerificationCodeUiState(
    stepText = stringResource(R.string.signup_step_1),
    title = stringResource(R.string.verification_code_title),
    description = stringResource(R.string.verification_code_description, "s20000@gsm.hs.kr"),
    code = "222222",
    expireText = stringResource(R.string.verification_code_expire, "04:52"),
)

@Composable
internal fun previewPasswordSetupUiState() = BookOnPasswordSetupUiState(
    stepText = stringResource(R.string.signup_step_2),
    title = stringResource(R.string.signup_account_info_title),
    description = stringResource(R.string.password_setup_description),
    password = BookOnPasswordFieldUiState("", label = stringResource(R.string.password), placeholder = stringResource(R.string.password)),
    passwordConfirm = BookOnPasswordFieldUiState(
        value = "",
        label = stringResource(R.string.password_confirm_short),
        placeholder = stringResource(R.string.password_confirm),
    ),
    ruleTitle = stringResource(R.string.password_precaution),
    ruleText = stringResource(R.string.password_rule),
    privacyChecked = false,
)

@Composable
internal fun previewPrivacyPolicyUiState() = BookOnPrivacyPolicyUiState(
    stepText = stringResource(R.string.signup_step_2),
    title = stringResource(R.string.privacy_policy_title),
    sections = listOf(
        BookOnPrivacyPolicySectionUiState(
            stringResource(R.string.privacy_collected_items_title),
            stringResource(R.string.privacy_collected_items),
        ),
        BookOnPrivacyPolicySectionUiState(
            stringResource(R.string.privacy_purpose_title),
            "${stringResource(R.string.privacy_purpose_account)}\n${stringResource(R.string.privacy_purpose_notice)}",
        ),
        BookOnPrivacyPolicySectionUiState(
            stringResource(R.string.privacy_retention_title),
            stringResource(R.string.privacy_retention_until_withdrawal),
        ),
    ),
    notice = stringResource(R.string.privacy_refusal_notice),
)

@Composable
internal fun previewReadingMarathonSignupUiState() = BookOnReadingMarathonSignupUiState(
    stepText = stringResource(R.string.signup_step_3),
    title = stringResource(R.string.reading_marathon),
    description = stringResource(R.string.reading_marathon_signup_description),
    useTitle = stringResource(R.string.reading_marathon_use),
    useDescription = stringResource(R.string.reading_marathon_use_description),
    benefitText = stringResource(R.string.reading_marathon_link_benefit),
    laterNotice = stringResource(R.string.reading_marathon_later_notice),
)

@Composable
internal fun previewReadingMarathonLinkUiState() = BookOnReadingMarathonLinkUiState(
    stepText = stringResource(R.string.signup_step_3),
    title = stringResource(R.string.reading_marathon_link_title),
    description = stringResource(R.string.reading_marathon_login_description),
    marathonId = BookOnTextFieldUiState(
        value = "",
        label = stringResource(R.string.reading_marathon_id),
        placeholder = "s20000@gsm.hs.kr",
    ),
    password = BookOnPasswordFieldUiState("", label = stringResource(R.string.password), placeholder = stringResource(R.string.password)),
    agreement = BookOnMarathonAgreementUiState(
        text = stringResource(R.string.reading_marathon_third_party_agreement),
        checked = false,
    ),
)

@Composable
internal fun previewSignupCompleteUiState() = BookOnSignupCompleteUiState(
    title = stringResource(R.string.signup_complete_title),
    message = stringResource(R.string.signup_complete_message, "홍길동"),
    ownedBookCountText = "4,218",
    marathonStatusText = stringResource(R.string.status_linked),
)
