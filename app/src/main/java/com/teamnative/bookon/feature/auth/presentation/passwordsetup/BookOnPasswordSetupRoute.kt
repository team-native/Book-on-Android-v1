package com.teamnative.bookon.feature.auth.presentation.passwordsetup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var privacyChecked by rememberSaveable { mutableStateOf(false) }
    var privacyPolicyExpanded by rememberSaveable { mutableStateOf(false) }
    val uiState = samplePasswordSetupUiState().copy(
        password = BookOnPasswordFieldUiModel(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            errorText = password.takeIf { it.isNotEmpty() && !BookOnPasswordPolicy.isValid(it) }
                ?.let { stringResource(R.string.error_password_rule) },
        ),
        passwordConfirm = BookOnPasswordFieldUiModel(
            value = passwordConfirm,
            label = stringResource(R.string.password_confirm_short),
            placeholder = stringResource(R.string.password_confirm),
            errorText = passwordConfirm.takeIf { it.isNotEmpty() && it != password }
                ?.let { stringResource(R.string.error_password_mismatch) },
        ),
        privacyChecked = privacyChecked,
        privacyPolicyExpanded = privacyPolicyExpanded,
        nextEnabled = BookOnPasswordPolicy.isValid(password) &&
            password == passwordConfirm &&
            privacyChecked,
    )

    BookOnPasswordSetupScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPasswordChange = { password = it },
        onPasswordConfirmChange = { passwordConfirm = it },
        onPrivacyCheckedChange = { privacyChecked = it },
        onPrivacyPolicyExpandedChange = { privacyPolicyExpanded = it },
        onNextClick = onNextClick,
        initialProgressStep = initialProgressStep,
    )
}
