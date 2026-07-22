package com.teamnative.bookon.feature.auth.presentation.passwordsetup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.component.BookOnPasswordPolicy

/** 비밀번호와 개인정보 동의 상태를 보존하며 화면 이벤트를 연결한다. */
@Composable
fun BookOnPasswordSetupRoute(
    initialProgressStep: Int?,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: com.teamnative.bookon.feature.auth.presentation.signup.BookOnRegistrationViewModel = hiltViewModel(),
) {
    val registrationState by viewModel.state.collectAsStateWithLifecycle()
    var privacyPolicyExpanded by rememberSaveable { mutableStateOf(false) }
    val uiState = defaultPasswordSetupUiState().copy(
        password = BookOnPasswordFieldUiModel(
            value = registrationState.password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            errorText = registrationState.password.takeIf { it.isNotEmpty() && !BookOnPasswordPolicy.isValid(it) }
                ?.let { stringResource(R.string.error_check_password_notice) },
        ),
        passwordConfirm = BookOnPasswordFieldUiModel(
            value = registrationState.passwordConfirm,
            label = stringResource(R.string.password_confirm_short),
            placeholder = stringResource(R.string.password_confirm),
            errorText = registrationState.passwordConfirm.takeIf { it.isNotEmpty() && it != registrationState.password }
                ?.let { stringResource(R.string.error_check_password_again) },
        ),
        privacyChecked = registrationState.privacyAccepted,
        privacyPolicyExpanded = privacyPolicyExpanded,
        isVerificationRequestInProgress = registrationState.isLoading,
        nextEnabled = BookOnPasswordPolicy.isValid(registrationState.password) &&
            registrationState.password == registrationState.passwordConfirm &&
            registrationState.privacyAccepted &&
            !registrationState.isLoading,
    )

    BookOnPasswordSetupScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPasswordChange = { password -> viewModel.update { it.copy(password = password) } },
        onPasswordConfirmChange = { password -> viewModel.update { it.copy(passwordConfirm = password) } },
        onPrivacyCheckedChange = { accepted -> viewModel.update { it.copy(privacyAccepted = accepted) } },
        onPrivacyPolicyExpandedChange = { privacyPolicyExpanded = it },
        onNextClick = { viewModel.requestVerification(onNextClick) },
        initialProgressStep = initialProgressStep,
    )
}
