package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.remember
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

/**
 * 독서마라톤 연동을 건너뛰는 문구에서 행동 텍스트만 강조한다.
 * 문구는 리소스에서 읽고 스타일은 상수로 관리해 다국어 대응과 스타일 일관성을 유지한다.
 */
@Composable
internal fun skipReadingMarathonAnnotatedString(): AnnotatedString {
    val prefix = stringResource(R.string.skip_reading_marathon_prefix)
    val action = stringResource(R.string.skip_reading_marathon_action)
    val actionStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
    )

    return remember(prefix, action, actionStyle) {
        AnnotatedString(
            text = prefix + action,
            spanStyles = listOf(
                AnnotatedString.Range(
                    item = actionStyle,
                    start = prefix.length,
                    end = prefix.length + action.length,
                ),
            ),
        )
    }
}

/**
 * 독서마라톤 연동을 나중으로 미루는 하단 텍스트 액션이다.
 * 클릭 처리는 회원가입 흐름의 navigation callback으로 위임한다.
 */
@Composable
fun BookOnSkipTextButton(
    text: AnnotatedString,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        text = text,
        style = bookOnTypography.caption,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}
