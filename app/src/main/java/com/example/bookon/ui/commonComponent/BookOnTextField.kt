package com.example.bookon.ui.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.example.bookon.R
import com.example.bookon.ui.theme.AppComponentSize
import com.example.bookon.ui.theme.AppElevation
import com.example.bookon.ui.theme.AppIconSize
import com.example.bookon.ui.theme.AppRadius
import com.example.bookon.ui.theme.AppSpacing
import com.example.bookon.ui.theme.BookOnColor
import com.example.bookon.ui.theme.BookOnTheme
import com.example.bookon.ui.theme.BookOnTypography
import com.example.bookon.uiState.BookOnPasswordFieldUiState
import com.example.bookon.uiState.BookOnTextFieldUiState

/**
 * 라벨, 아이콘, 오류 문구를 포함한 BookOn 입력 필드이다.
 * 화면은 value와 onValueChange만 전달하고 검증 결과는 isError와 errorText로 표현한다.
 */
@Composable
fun BookOnTextField(
    uiState: BookOnTextFieldUiState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    textStyle: TextStyle = BookOnTypography.fieldText,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    BookOnTextField(
        value = uiState.value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = uiState.label,
        placeholder = uiState.placeholder,
        errorText = uiState.errorText,
        isError = uiState.isError,
        enabled = uiState.enabled,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

/**
 * 라벨, 아이콘, 오류 문구를 포함한 BookOn 입력 필드이다.
 * 화면은 value와 onValueChange만 전달하고 검증 결과는 isError와 errorText로 표현한다.
 */
@Composable
fun BookOnTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    errorText: String? = null,
    isError: Boolean = errorText != null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    textStyle: TextStyle = BookOnTypography.fieldText,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = BookOnTypography.fieldLabel,
                color = BookOnColor.TextPrimary,
            )
            Spacer(modifier = Modifier.height(AppSpacing.Small))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppComponentSize.FieldHeight)
                .shadow(
                    elevation = AppElevation.Field,
                    shape = RoundedCornerShape(AppRadius.Field),
                )
                .clip(RoundedCornerShape(AppRadius.Field))
                .background(if (isError) BookOnColor.ErrorContainer else BookOnColor.Surface),
            enabled = enabled,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = BookOnColor.TextPrimary),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(BookOnColor.Primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppSpacing.Content),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (leadingIcon != null) {
                        Box(
                            modifier = Modifier.size(AppIconSize.Small),
                            contentAlignment = Alignment.Center,
                        ) {
                            leadingIcon()
                        }
                        Spacer(modifier = Modifier.width(AppSpacing.Item))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = BookOnTypography.fieldPlaceholder,
                                color = BookOnColor.TextPlaceholder,
                            )
                        }
                        innerTextField()
                    }

                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(AppSpacing.Item))
                        Box(
                            modifier = Modifier.size(AppIconSize.Default),
                            contentAlignment = Alignment.Center,
                        ) {
                            trailingIcon()
                        }
                    }
                }
            },
        )

        if (errorText != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Small))
            Text(
                text = errorText,
                style = BookOnTypography.caption,
                color = BookOnColor.Error,
            )
        }
    }
}

/**
 * 비밀번호 표시 전환을 포함한 입력 필드이다.
 * trailingIcon을 직접 넘기지 않아도 텍스트 토글로 표시 상태를 제어한다.
 */
@Composable
fun BookOnPasswordField(
    uiState: BookOnPasswordFieldUiState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visibleLabel: String? = null,
    hiddenLabel: String? = null,
) {
    BookOnPasswordField(
        value = uiState.value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = uiState.label,
        placeholder = uiState.placeholder,
        errorText = uiState.errorText,
        enabled = uiState.enabled,
        visibleLabel = visibleLabel,
        hiddenLabel = hiddenLabel,
    )
}

/**
 * 비밀번호 표시 전환을 포함한 입력 필드이다.
 * trailingIcon을 직접 넘기지 않아도 텍스트 토글로 표시 상태를 제어한다.
 */
@Composable
fun BookOnPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    errorText: String? = null,
    enabled: Boolean = true,
    visibleLabel: String? = null,
    hiddenLabel: String? = null,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val showPasswordText = hiddenLabel ?: stringResource(R.string.action_show_password)
    val hidePasswordText = visibleLabel ?: stringResource(R.string.action_hide_password)

    BookOnTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        errorText = errorText,
        enabled = enabled,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            Text(
                modifier = Modifier.clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = { passwordVisible = !passwordVisible },
                ),
                text = if (passwordVisible) hidePasswordText else showPasswordText,
                style = BookOnTypography.caption,
                color = BookOnColor.TextPlaceholder,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnTextFieldPreview() {
    BookOnTheme {
        BookOnTextField(
            value = "",
            onValueChange = {},
            label = "학교 이메일",
            placeholder = "이메일 주소",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnTextFieldErrorPreview() {
    BookOnTheme {
        BookOnTextField(
            value = "student",
            onValueChange = {},
            label = "학교 이메일",
            errorText = "올바른 이메일 형식이 아니에요",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnPasswordFieldPreview() {
    BookOnTheme {
        BookOnPasswordField(
            value = "password",
            onValueChange = {},
            label = "비밀번호",
        )
    }
}
