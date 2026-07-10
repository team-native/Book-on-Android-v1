package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

/** 독서마라톤 계정 입력과 동의 상태를 보존하며 화면 이벤트를 연결한다. */
@Composable
fun BookOnReadingMarathonLinkRoute(
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    var marathonId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var agreementChecked by rememberSaveable { mutableStateOf(false) }
    val sample = sampleReadingMarathonLinkUiState()
    val uiState = sample.copy(
        marathonId = BookOnTextFieldUiModel(
            value = marathonId,
            label = stringResource(R.string.reading_marathon_id),
            placeholder = stringResource(R.string.reading_marathon_id),
        ),
        password = BookOnPasswordFieldUiModel(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
        ),
        agreement = sample.agreement.copy(checked = agreementChecked),
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
