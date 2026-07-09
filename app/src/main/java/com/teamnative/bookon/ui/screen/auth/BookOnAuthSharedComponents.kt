package com.teamnative.bookon.ui.screen.auth

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppElevation
import com.teamnative.bookon.theme.AppIconSize
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.ui.Component.bar.BookOnTopBar
import com.teamnative.bookon.ui.Component.textfield.BookOnPasswordField
import com.teamnative.bookon.ui.Component.textfield.BookOnTextField
import com.teamnative.bookon.uiState.BookOnPasswordFieldUiState
import com.teamnative.bookon.uiState.BookOnTextFieldUiState

internal val LoginTitleTopSpacing = 140.dp
internal val AuthTitleTopSpacing = 28.dp
internal val AuthOauthTopSpacing = 28.dp

private val PrivacyCardHeight = 88.dp
private val MarathonCardHeight = 92.dp
private val MarathonNoticeHeight = 67.dp

@Composable
internal fun BookOnAuthTopBar(
    onBackClick: () -> Unit,
    onPasswordNoticeClick: () -> Unit,
) {
    BookOnTopBar(
        title = "",
        onBackClick = onBackClick,
        trailingContent = {
            Row(
                modifier = Modifier.clickable(role = Role.Button, onClick = onPasswordNoticeClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.password_warning_action),
                    style = BookOnTypography.caption.copy(fontWeight = FontWeight.Normal),
                    color = BookOnColor.TextTertiary,
                )
                Spacer(modifier = Modifier.width(AppSpacing.Tiny))
                Text(
                    text = stringResource(R.string.info_mark),
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextTertiary,
                )
            }
        },
    )
}

@Composable
internal fun BookOnEmailField(
    uiState: BookOnTextFieldUiState,
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
    uiState: BookOnTextFieldUiState,
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
    uiState: BookOnPasswordFieldUiState,
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

@Composable
internal fun BookOnPrivacyAgreementCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(PrivacyCardHeight)
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(BookOnColor.Background)
            .border(1.dp, BookOnColor.SurfaceBorder, RoundedCornerShape(AppRadius.Field))
            .padding(horizontal = AppSpacing.Content, vertical = AppSpacing.Content),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.privacy_policy_title),
                style = BookOnTypography.caption,
                color = BookOnColor.TextPrimary,
            )
            Image(
                modifier = Modifier.size(width = 13.dp, height = 6.dp),
                painter = painterResource(R.drawable.down_arrow),
                contentDescription = stringResource(R.string.privacy_policy_expand_description),
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.Item))
        BookOnCheckTextRow(
            text = stringResource(R.string.privacy_required_agreement),
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun BookOnCheckTextRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(AppRadius.Progress))
                .clickable(role = Role.Checkbox, onClick = { onCheckedChange(!checked) }),
            painter = painterResource(if (checked) R.drawable.authority_check else R.drawable.authority_not_check),
            contentDescription = stringResource(R.string.privacy_agreement_check_description),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = BookOnTypography.caption.copy(fontWeight = FontWeight.Medium),
            color = BookOnColor.TextPrimary,
        )
    }
}

@Composable
internal fun BookOnMarathonToggleCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MarathonCardHeight)
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(BookOnColor.Background)
            .border(2.dp, BookOnColor.Divider, RoundedCornerShape(AppRadius.Field))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppIconSize.XLarge)
                .clip(RoundedCornerShape(AppRadius.IconButton))
                .background(BookOnColor.PrimaryLight),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(AppIconSize.Default),
                painter = painterResource(R.drawable.marathon_logo),
                contentDescription = stringResource(R.string.reading_marathon_logo_description),
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(modifier = Modifier.width(AppSpacing.Content))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = BookOnTypography.bodySemiBold, color = BookOnColor.TextPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.Tiny))
            Text(text = description, style = BookOnTypography.caption, color = BookOnColor.TextTertiary)
        }
        Switch(checked = true, onCheckedChange = { onClick() })
    }
}

@Composable
internal fun BookOnMarathonNoticeCard(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MarathonNoticeHeight)
            .shadow(AppElevation.StrongCard, RoundedCornerShape(AppRadius.Field))
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(BookOnColor.PrimaryContainer)
            .padding(horizontal = AppSpacing.Content, vertical = AppSpacing.Item),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = stringResource(R.string.info_mark),
            style = BookOnTypography.caption,
            color = BookOnColor.PrimaryPressed,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = BookOnTypography.caption,
            color = BookOnColor.TextDarkGray,
        )
    }
}

@Composable
internal fun BookOnThirdPartyAgreementRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(AppRadius.Progress))
                .clickable(role = Role.Checkbox, onClick = { onCheckedChange(!checked) }),
            painter = painterResource(if (checked) R.drawable.authority_check else R.drawable.authority_not_check),
            contentDescription = stringResource(R.string.reading_marathon_agreement_check_description),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                append(stringResource(R.string.reading_marathon_third_party_agreement_prefix))
                withStyle(SpanStyle(color = BookOnColor.Primary, fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.reading_marathon_third_party_agreement_highlight))
                }
                append(stringResource(R.string.reading_marathon_third_party_agreement_suffix))
            },
            style = BookOnTypography.caption,
            color = BookOnColor.TextTertiary,
        )
    }
}
