package com.example.bookon.ui.commonComponent.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.example.bookon.R
import com.example.bookon.theme.AppComponentSize
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.BookOnPasswordFieldUiState

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
    val toggleDescription = stringResource(R.string.password_visibility_toggle_description)

    BookOnTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        errorText = errorText,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            Box(
                modifier = Modifier
                    .sizeIn(
                        minWidth = AppComponentSize.MinTouchTarget,
                        minHeight = AppComponentSize.MinTouchTarget,
                    )
                    .semantics {
                        contentDescription = toggleDescription
                    }
                    .clickable(
                        enabled = enabled,
                        role = Role.Button,
                        onClick = { passwordVisible = !passwordVisible },
                    )
                    .padding(horizontal = AppSpacing.Small),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (passwordVisible) hidePasswordText else showPasswordText,
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextPlaceholder,
                )
            }
        },
    )
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
