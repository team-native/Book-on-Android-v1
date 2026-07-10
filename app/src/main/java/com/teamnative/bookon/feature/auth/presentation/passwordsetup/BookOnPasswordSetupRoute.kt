package com.teamnative.bookon.feature.auth.presentation.passwordsetup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel

/** 비밀번호와 개인정보 동의 상태를 보존하며 화면 이벤트를 연결한다. */
@Composable
fun BookOnPasswordSetupRoute(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var privacyChecked by rememberSaveable { mutableStateOf(false) }
    var privacyPolicyExpanded by rememberSaveable { mutableStateOf(false) }
    val uiState = samplePasswordSetupUiState().copy(
        password = BookOnPasswordFieldUiModel(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
        ),
        passwordConfirm = BookOnPasswordFieldUiModel(
            value = passwordConfirm,
            label = stringResource(R.string.password_confirm_short),
            placeholder = stringResource(R.string.password_confirm),
        ),
        privacyChecked = privacyChecked,
        privacyPolicyExpanded = privacyPolicyExpanded,
    )

    BookOnPasswordSetupScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPasswordNoticeClick = {},
        onPasswordChange = { password = it },
        onPasswordConfirmChange = { passwordConfirm = it },
        onPrivacyCheckedChange = { privacyChecked = it },
        onPrivacyPolicyExpandedChange = { privacyPolicyExpanded = it },
        onNextClick = onNextClick,
    )
}
