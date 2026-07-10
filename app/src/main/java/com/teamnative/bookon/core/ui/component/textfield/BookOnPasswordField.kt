package com.teamnative.bookon.core.ui.component.textfield

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel

/**
 * 비밀번호 표시 전환을 포함한 입력 필드이다.
 * trailingIcon을 직접 넘기지 않아도 이미지 토글로 표시 상태를 제어한다.
 */
@Composable
fun BookOnPasswordField(
    uiState: BookOnPasswordFieldUiModel,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visibleLabel: String? = null,
    hiddenLabel: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
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
        leadingIcon = leadingIcon,
    )
}

/**
 * 비밀번호 표시 전환을 포함한 입력 필드이다.
 * trailingIcon을 직접 넘기지 않아도 이미지 토글로 표시 상태를 제어한다.
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
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val showPasswordText = hiddenLabel ?: stringResource(R.string.action_show_password)
    val hidePasswordText = visibleLabel ?: stringResource(R.string.action_hide_password)
    val toggleDescription = if (passwordVisible) hidePasswordText else showPasswordText
    val toggleIconRes = if (passwordVisible) {
        R.drawable.password_hide
    } else {
        R.drawable.pawward_visible
    }
    val toggleInteractionSource = remember { MutableInteractionSource() }
    val textFieldInteractionSource = remember { MutableInteractionSource() }
    val isFocused by textFieldInteractionSource.collectIsFocusedAsState()
    val isError = errorText != null
    val shape = RoundedCornerShape(AppRadius.Field)
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> BookOnColor.Error
            isFocused -> BookOnColor.Primary
            else -> BookOnColor.Surface
        },
    )
    val containerColor by animateColorAsState(
        targetValue = if (isError) BookOnColor.ErrorContainer else BookOnColor.Surface,
    )

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
                    shape = shape,
                )
                .clip(shape)
                .background(containerColor)
                .border(
                    width = AppComponentSize.FieldBorderWidth,
                    color = borderColor,
                    shape = shape,
                )
                .semantics {
                    if (errorText != null) error(errorText)
                },
            enabled = enabled,
            singleLine = true,
            textStyle = BookOnTypography.fieldText.copy(color = BookOnColor.TextPrimary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            cursorBrush = SolidColor(BookOnColor.Primary),
            interactionSource = textFieldInteractionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = AppSpacing.FieldHorizontal,
                            end = AppSpacing.FieldHorizontal,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (leadingIcon != null) {
                        Box(
                            modifier = Modifier.size(AppIconSize.Small),
                            contentAlignment = Alignment.Center,
                        ) {
                            leadingIcon()
                        }
                        Spacer(modifier = Modifier.width(AppSpacing.Content))
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

                    Spacer(modifier = Modifier.width(AppSpacing.Item))

                    Icon(
                        painter = painterResource(id = toggleIconRes),
                        contentDescription = toggleDescription,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(AppIconSize.Small)
                            .combinedClickable(
                                interactionSource = toggleInteractionSource,
                                indication = null,
                                enabled = enabled,
                                role = Role.Button,
                                onClick = { passwordVisible = !passwordVisible },
                            ),
                    )
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
