package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

private const val ThirdPartyAgreementAnnotationTag = "third_party_agreement"
private val ThirdPartyAgreementCheckIconSize = 16.dp

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
                .size(ThirdPartyAgreementCheckIconSize)
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
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = ThirdPartyAgreementAnnotationTag,
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = BookOnColor.Primary,
                                fontWeight = FontWeight.Bold,
                            ),
                        ),
                        linkInteractionListener = {
                            // 개인정보 제3자 제공 안내 팝업이 준비되면 이 이벤트에 연결한다.
                        },
                    ),
                ) {
                    append(stringResource(R.string.reading_marathon_third_party_agreement_highlight))
                }
                append(stringResource(R.string.reading_marathon_third_party_agreement_suffix))
            },
            style = BookOnTypography.caption,
            color = BookOnColor.TextTertiary,
        )
    }
}
