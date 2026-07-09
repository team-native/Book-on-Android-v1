package com.teamnative.bookon.ui.Component.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppElevation
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography

private const val VerificationCodeLength = 6

private val VerificationBoxHeight = 52.dp

/**
 * 인증번호 6자리를 같은 너비 박스로 표시하는 입력 컴포넌트이다.
 * 실제 검증과 재전송 이벤트는 호출 화면에서 처리한다.
 */
@Composable
fun BookOnVerificationCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    BasicTextField(
        value = code,
        onValueChange = { value ->
            onCodeChange(value.filter(Char::isDigit).take(VerificationCodeLength))
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = BookOnTypography.sectionTitle.copy(
            color = BookOnColor.TextPrimary,
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        cursorBrush = SolidColor(BookOnColor.Primary),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small),
            ) {
                repeat(VerificationCodeLength) { index ->
                    val character = code.getOrNull(index)?.toString().orEmpty()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(VerificationBoxHeight)
                            .shadow(
                                elevation = AppElevation.Field,
                                shape = RoundedCornerShape(AppRadius.Field),
                            )
                            .clip(RoundedCornerShape(AppRadius.Field))
                            .background(
                                if (isError) {
                                    BookOnColor.ErrorContainer
                                } else {
                                    BookOnColor.Surface
                                },
                            )
                            .padding(horizontal = AppSpacing.Small),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = character,
                            style = BookOnTypography.sectionTitle,
                            color = BookOnColor.TextPrimary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnVerificationCodeFieldPreview() {
    BookOnTheme {
        BookOnVerificationCodeField(
            code = "2222",
            onCodeChange = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
