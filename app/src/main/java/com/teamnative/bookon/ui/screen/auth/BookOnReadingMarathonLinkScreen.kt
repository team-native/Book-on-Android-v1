package com.teamnative.bookon.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.ui.Component.auth.BookOnAuthFormScaffold
import com.teamnative.bookon.ui.Component.auth.BookOnSignupStepHeader
import com.teamnative.bookon.ui.Component.auth.BookOnSkipTextButton
import com.teamnative.bookon.ui.Component.bar.BookOnTopBar
import com.teamnative.bookon.ui.Component.button.BookOnPrimaryButton
import com.teamnative.bookon.ui.Component.marathon.BookOnMarathonCircleAction
import com.teamnative.bookon.ui.Component.textfield.BookOnTextField
import com.teamnative.bookon.uiState.auth.BookOnReadingMarathonLinkUiState

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
    onOauthClick: () -> Unit,
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
        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))
        BookOnSignupStepHeader(
            step = 3,
            title = uiState.title,
            description = uiState.description,
            animateProgress = false,
        )
        BookOnTextField(uiState = uiState.marathonId, onValueChange = onIdChange)
        BookOnAuthPasswordField(uiState = uiState.password, onValueChange = onPasswordChange)
        BookOnThirdPartyAgreementRow(
            checked = uiState.agreement.checked,
            onCheckedChange = onAgreementChange,
        )
        Spacer(modifier = Modifier.height(AuthOauthTopSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BookOnMarathonCircleAction(
                iconRes = R.drawable.oauth_google,
                contentDescription = stringResource(R.string.oauth_google_description),
                onSelected = onOauthClick,
            )
            Spacer(modifier = Modifier.width(AppSpacing.Section))
            BookOnMarathonCircleAction(
                iconRes = R.drawable.oauth_naver,
                contentDescription = stringResource(R.string.oauth_naver_description),
                onSelected = onOauthClick,
            )
            Spacer(modifier = Modifier.width(AppSpacing.Section))
            BookOnMarathonCircleAction(
                iconRes = R.drawable.oauth_kakao,
                contentDescription = stringResource(R.string.oauth_kakao_description),
                onSelected = onOauthClick,
            )
        }
    }
}
