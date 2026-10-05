package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnMarathonAgreementUiModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookOnReadingMarathonLinkScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun read365WebsiteButton_invokesOpenCallback() {
        var isRead365WebsiteOpened = false

        composeTestRule.setContent {
            BookOnTheme {
                BookOnReadingMarathonLinkScreen(
                    uiState = createReadingMarathonLinkUiState(),
                    onBackClick = {},
                    onIdChange = {},
                    onPasswordChange = {},
                    onAgreementChange = {},
                    onOpenRead365Click = { isRead365WebsiteOpened = true },
                    onOauthClick = {},
                    onSkipClick = {},
                    onCompleteClick = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText("Read365 웹사이트 열기")
            .performClick()

        composeTestRule.runOnIdle {
            assertTrue(isRead365WebsiteOpened)
        }
    }

    private fun createReadingMarathonLinkUiState() = BookOnReadingMarathonLinkUiState(
        stepText = "STEP 3 / 3",
        title = "계정연동",
        description = "독서마라톤 아이디와 비밀번호를 입력해 주세요",
        marathonId = BookOnTextFieldUiModel(
            value = "",
            label = "독서마라톤 아이디",
            placeholder = "독서마라톤 아이디",
        ),
        password = BookOnPasswordFieldUiModel(
            value = "",
            label = "비밀번호",
            placeholder = "비밀번호",
        ),
        agreement = BookOnMarathonAgreementUiModel(
            text = "독서마라톤 계정 연동을 위한 개인정보 제3자 제공에 동의합니다.",
            checked = false,
        ),
    )
}
