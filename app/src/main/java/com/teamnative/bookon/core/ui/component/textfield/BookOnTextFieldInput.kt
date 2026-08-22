package com.teamnative.bookon.core.ui.component.textfield

import androidx.compose.material3.MaterialTheme

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

/**
 * 라벨, 아이콘, 오류 문구를 포함한 BookOn 입력 필드이다.
 * 화면은 value와 onValueChange만 전달하고 검증 결과는 isError와 errorText로 표현한다.
 */
@Composable
fun BookOnTextField(
    uiState: BookOnTextFieldUiModel,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    textStyle: TextStyle = bookOnTypography.fieldText,
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
        suffixText = uiState.suffixText,
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
    suffixText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    textStyle: TextStyle = bookOnTypography.fieldText,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(AppRadius.Field)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> MaterialTheme.colorScheme.errorContainer
            isFocused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        },
    )
    val containerColor by animateColorAsState(
        targetValue = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,
    )

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = bookOnTypography.fieldLabel,
                color = MaterialTheme.colorScheme.onSurface,
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
                    if (isError && errorText != null) error(errorText)
                },
            enabled = enabled,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppSpacing.FieldHorizontal),
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
                                style = bookOnTypography.fieldPlaceholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        innerTextField()
                    }

                    if (suffixText != null) {

                        Spacer(modifier = Modifier.width(AppSpacing.Item))

                        Text(
                            text = suffixText,
                            style = bookOnTypography.fieldPlaceholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    if (trailingIcon != null) {

                        Spacer(modifier = Modifier.width(AppSpacing.Item))

                        Box(
                            modifier = Modifier.sizeIn(
                                minWidth = AppComponentSize.MinTouchTarget,
                                minHeight = AppComponentSize.MinTouchTarget,
                            ),
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
                style = bookOnTypography.caption,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
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
