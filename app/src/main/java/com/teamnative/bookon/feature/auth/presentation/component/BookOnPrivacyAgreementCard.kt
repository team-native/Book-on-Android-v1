package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppAnimationDuration
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

@Composable
internal fun BookOnPrivacyAgreementCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.Field))
            .background(BookOnColor.Surface)
            .border(1.dp, BookOnColor.SurfaceBorder, RoundedCornerShape(AppRadius.Field))
            .animateContentSize(animationSpec = tween(durationMillis = AppAnimationDuration.Medium))
            .padding(AppSpacing.Content),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 24.dp)
                .clickable(
                    role = Role.Button,
                    onClickLabel = stringResource(
                        if (expanded) {
                            R.string.privacy_policy_collapse_description
                        } else {
                            R.string.privacy_policy_expand_description
                        },
                    ),
                    onClick = { onExpandedChange(!expanded) },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.privacy_policy_title),
                style = BookOnTypography.caption,
                color = BookOnColor.TextPrimary,
            )
            Image(
                modifier = Modifier.size(width = 13.dp, height = 6.dp),
                painter = painterResource(if (expanded) R.drawable.up_arrow else R.drawable.down_arrow),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }
        if (expanded) {
            BookOnPrivacyPolicyDetails()
            Spacer(modifier = Modifier.height(AppSpacing.Content))
        } else {
            Spacer(modifier = Modifier.height(AppSpacing.Item))
        }
        BookOnCheckTextRow(
            text = stringResource(R.string.privacy_required_agreement),
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun BookOnPrivacyPolicyDetails() {
    Spacer(modifier = Modifier.height(AppSpacing.Content))
    BookOnPrivacyPolicyDetail(
        title = stringResource(R.string.privacy_collected_items_title),
        body = stringResource(R.string.privacy_collected_items),
    )
    Spacer(modifier = Modifier.height(AppSpacing.Item))
    BookOnPrivacyPolicyDetail(
        title = stringResource(R.string.privacy_purpose_title),
        body = listOf(
            stringResource(R.string.privacy_bullet_format, stringResource(R.string.privacy_purpose_account)),
            stringResource(R.string.privacy_bullet_format, stringResource(R.string.privacy_purpose_notice)),
        ).joinToString("\n"),
    )
    Spacer(modifier = Modifier.height(AppSpacing.Item))
    BookOnPrivacyPolicyDetail(
        title = stringResource(R.string.privacy_retention_title),
        body = stringResource(R.string.privacy_retention_until_withdrawal),
    )
    Spacer(modifier = Modifier.height(AppSpacing.Item))
    BookOnPrivacyPolicyDetail(
        title = stringResource(R.string.terms_title),
        body = stringResource(
            R.string.privacy_bullet_format,
            stringResource(R.string.terms_service_interruption),
        ),
    )
    Spacer(modifier = Modifier.height(AppSpacing.Content))
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.Small))
            .background(BookOnColor.Background)
            .border(1.dp, BookOnColor.SurfaceBorder, RoundedCornerShape(AppRadius.Small))
            .padding(AppSpacing.Item),
        text = stringResource(R.string.privacy_refusal_notice),
        style = BookOnTypography.privacyNotice,
        color = BookOnColor.TextSecondary,
    )
}

@Composable
private fun BookOnPrivacyPolicyDetail(
    title: String,
    body: String,
) {
    Text(text = title, style = BookOnTypography.caption, color = BookOnColor.TextPrimary)
    Spacer(modifier = Modifier.height(AppSpacing.Small))
    Text(
        text = body,
        style = BookOnTypography.caption.copy(fontWeight = FontWeight.Normal),
        color = BookOnColor.TextSecondary,
    )
}

@Composable
private fun BookOnCheckTextRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkDescription = stringResource(R.string.privacy_agreement_check_description)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(AppRadius.Progress))
                .toggleable(
                    value = checked,
                    role = Role.Checkbox,
                    onValueChange = onCheckedChange,
                ),
            painter = painterResource(if (checked) R.drawable.authority_check else R.drawable.authority_not_check),
            contentDescription = checkDescription,
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
