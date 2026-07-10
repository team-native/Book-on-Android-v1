package com.teamnative.bookon.feature.auth.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.ui.component.textfield.BookOnPasswordField
import com.teamnative.bookon.core.ui.component.textfield.BookOnTextField
import com.teamnative.bookon.core.ui.model.BookOnPasswordFieldUiModel
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel

@Composable
internal fun BookOnEmailField(
    uiState: BookOnTextFieldUiModel,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnTextField(
        uiState = uiState.copy(suffixText = uiState.suffixText ?: stringResource(R.string.email_domain_gsm)),
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = {
            BookOnAuthFieldIcon(
                iconRes = R.drawable.email,
                contentDescription = stringResource(R.string.email_icon_description),
            )
        },
    )
}

@Composable
internal fun BookOnNameField(
    uiState: BookOnTextFieldUiModel,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnTextField(
        uiState = uiState,
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = {
            BookOnAuthFieldIcon(
                iconRes = R.drawable.name,
                contentDescription = stringResource(R.string.name_icon_description),
            )
        },
    )
}

@Composable
internal fun BookOnAuthPasswordField(
    uiState: BookOnPasswordFieldUiModel,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnPasswordField(
        uiState = uiState,
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = {
            BookOnAuthFieldIcon(
                iconRes = R.drawable.password_lock,
                contentDescription = stringResource(R.string.password_icon_description),
            )
        },
    )
}

@Composable
private fun BookOnAuthFieldIcon(
    @DrawableRes iconRes: Int,
    contentDescription: String,
) {
    Image(
        modifier = Modifier.size(AppIconSize.Small),
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
    )
}
