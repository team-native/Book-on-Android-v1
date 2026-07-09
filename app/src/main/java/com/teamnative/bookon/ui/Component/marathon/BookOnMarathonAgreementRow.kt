package com.teamnative.bookon.ui.Component.marathon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.marathon.BookOnMarathonAgreementUiState

private val MarathonAgreementCheckSize = 16.dp
private val MarathonAgreementCheckRadius = 13.dp

private const val MarathonComponentEnabledAlpha = 1f
private const val MarathonComponentDisabledAlpha = 0.55f

private const val MarathonAgreementRowPreviewText = "독서마라톤 계정 연동을 위한 개인정보 제3자 제공에 동의합니다."

/**
 * 독서마라톤 계정 연동 동의처럼 체크 이미지와 문구를 함께 표시한다.
 * 체크 이미지만 클릭 가능하며, checked 상태에 따라 선택 이미지를 표시한다.
 */
@Composable
fun BookOnMarathonAgreementRow(
    uiState: BookOnMarathonAgreementUiState,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnMarathonAgreementRow(
        text = uiState.text,
        checked = uiState.checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = uiState.enabled,
    )
}

/**
 * 독서마라톤 계정 연동 동의처럼 체크 이미지와 문구를 함께 표시한다.
 * 체크 이미지만 클릭 가능하며, checked 상태에 따라 선택 이미지를 표시한다.
 */
@Composable
fun BookOnMarathonAgreementRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) MarathonComponentEnabledAlpha else MarathonComponentDisabledAlpha),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            modifier = Modifier
                .size(MarathonAgreementCheckSize)
                .clip(RoundedCornerShape(MarathonAgreementCheckRadius))
                .clickable(
                    enabled = enabled,
                    role = Role.Checkbox,
                    onClick = { onCheckedChange(!checked) },
                ),
            painter = painterResource(
                if (checked) {
                    R.drawable.authority_check
                } else {
                    R.drawable.authority_not_check
                },
            ),
            contentDescription = stringResource(R.string.reading_marathon_agreement_check_description),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(AppSpacing.Small))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = BookOnTypography.caption,
            color = BookOnColor.TextTertiary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMarathonAgreementRowPreview() {
    BookOnTheme {
        BookOnMarathonAgreementRow(
            text = MarathonAgreementRowPreviewText,
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
