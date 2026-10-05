package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

private const val READ365_WEB_URL = "https://read365.gwangju.go.kr/"
private const val CHROME_PACKAGE_NAME = "com.android.chrome"

/** 독서마라톤 계정 입력과 동의 상태를 보존하며 화면 이벤트를 연결한다. */
@Composable
fun BookOnReadingMarathonLinkRoute(
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    val viewModel: BookOnReadingMarathonLinkViewModel = hiltViewModel()
    val currentContext = LocalContext.current
    val linkState by viewModel.state.collectAsStateWithLifecycle()
    var marathonId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var agreementChecked by rememberSaveable { mutableStateOf(false) }
    val defaultState = defaultReadingMarathonLinkUiState()
    val uiState = defaultState.copy(
        marathonId = BookOnTextFieldUiModel(
            value = marathonId,
            label = stringResource(R.string.reading_marathon_id),
            placeholder = stringResource(R.string.reading_marathon_id),
        ),
        password = BookOnPasswordFieldUiModel(
            value = password,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            errorText = null,
        ),
        agreement = defaultState.agreement.copy(checked = agreementChecked),
        linkEnabled = marathonId.isNotBlank() &&
            password.isNotBlank() &&
            agreementChecked && !linkState.isLoading,
        errorText = linkState.errorText,
        isLoading = linkState.isLoading,
    )

    BookOnReadingMarathonLinkScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onIdChange = { marathonId = it },
        onPasswordChange = { password = it },
        onAgreementChange = { agreementChecked = it },
        onOpenRead365Click = { openRead365Website(currentContext) },
        onOauthClick = {},
        onSkipClick = onSkipClick,
        onCompleteClick = { viewModel.link(marathonId, password, onCompleteClick) },
    )
}

/** Read365 웹사이트를 Chrome에서 우선 열고, Chrome이 없으면 기본 브라우저로 연다. */
private fun openRead365Website(context: Context) {
    val read365Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(READ365_WEB_URL),
    )
    val chromeIntent = Intent(read365Intent).setPackage(CHROME_PACKAGE_NAME)

    try {
        context.startActivity(chromeIntent)
    } catch (_: ActivityNotFoundException) {
        try {
            context.startActivity(read365Intent)
        } catch (_: ActivityNotFoundException) {
            // 브라우저가 없는 환경에서는 화면 흐름을 중단하지 않는다.
        }
    }
}
