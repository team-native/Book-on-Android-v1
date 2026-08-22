package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

private const val VerificationCodeLength = 6

private val VerificationFieldHeight = 52.dp
private val VerificationUnderlineWidth = 2.dp

/**
 * 인증번호 6자리를 하단선으로 구분해 표시하는 입력 컴포넌트이다.
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
        textStyle = bookOnTypography.sectionTitle.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0f),
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = 0f)),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                innerTextField()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
                ) {
                    repeat(VerificationCodeLength) { index ->
                        val character = code.getOrNull(index)?.toString().orEmpty()
                        val underlineColor = when {
                            isError -> MaterialTheme.colorScheme.error
                            index < code.length -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(VerificationFieldHeight)
                                .drawBehind {
                                    val underlineHeight = VerificationUnderlineWidth.toPx()
                                    drawRect(
                                        color = underlineColor,
                                        topLeft = Offset(x = 0f, y = size.height - underlineHeight),
                                        size = Size(width = size.width, height = underlineHeight),
                                    )
                                }
                                .padding(horizontal = AppSpacing.Small),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = character,
                                style = bookOnTypography.sectionTitle,
                                color = underlineColor,
                                textAlign = TextAlign.Center,
                            )
                        }
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
            code = "",
            onCodeChange = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
