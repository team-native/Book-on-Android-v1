package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthFormScaffold
import com.teamnative.bookon.feature.auth.presentation.component.BookOnDropdownField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnGenderSelector
import com.teamnative.bookon.feature.auth.presentation.component.BookOnSignupStepHeader
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.auth.presentation.component.AuthTitleTopSpacing
import com.teamnative.bookon.feature.auth.presentation.component.BookOnAuthTopBar
import com.teamnative.bookon.feature.auth.presentation.component.BookOnEmailField
import com.teamnative.bookon.feature.auth.presentation.component.BookOnNameField

/**
 * 회원가입 화면은 학교 이메일, 이름, 성별, 학과 입력 컴포넌트를 조립한다.
 */
@Composable
fun BookOnSignupScreen(
    uiState: BookOnSignupUiState,
    onEvent: (BookOnSignupScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var progressAnimating by remember { mutableStateOf(true) }

    BookOnAuthFormScaffold(
        modifier = modifier,
        topBar = {
            BookOnAuthTopBar(
                onBackClick = { onEvent(BookOnSignupScreenEvent.BackClicked) },
            )
        },
        footer = {
            BookOnPrimaryButton(
                text = stringResource(R.string.action_next),
                onClick = { onEvent(BookOnSignupScreenEvent.NextClicked) },
                enabled = uiState.nextEnabled && !progressAnimating,
            )
        },
    ) {

        Spacer(modifier = Modifier.height(AuthTitleTopSpacing))

        BookOnSignupStepHeader(
            step = 1,
            title = uiState.title,
            description = uiState.description,
            onProgressAnimationRunningChange = { progressAnimating = it },
        )
        BookOnEmailField(
            uiState = uiState.email,
            onValueChange = { email -> onEvent(BookOnSignupScreenEvent.EmailChanged(email)) },
        )
        BookOnNameField(
            uiState = uiState.name,
            onValueChange = { name -> onEvent(BookOnSignupScreenEvent.NameChanged(name)) },
        )
        BookOnGenderSelector(
            selectedGender = uiState.selectedGender,
            onGenderSelected = { gender -> onEvent(BookOnSignupScreenEvent.GenderSelected(gender)) },
        )
        BookOnDropdownField(
            uiState = uiState.department,
            onClick = { onEvent(BookOnSignupScreenEvent.DepartmentClicked) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSignupScreenPreview() {
    BookOnTheme {
        BookOnSignupScreen(
            uiState = defaultSignupUiState(),
            onEvent = {},
        )
    }
}
