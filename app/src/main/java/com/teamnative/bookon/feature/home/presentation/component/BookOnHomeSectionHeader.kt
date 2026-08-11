package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
/**
 * 홈 화면 섹션 제목과 선택적 우측 액션을 한 줄로 표시한다.
 * actionText가 null이거나 onActionClick이 null이면 제목만 표시한다.
 */
@Composable
fun BookOnHomeSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    titleStyle: TextStyle = bookOnTypography.sectionTitle,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (actionText != null && onActionClick != null) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppRadius.Small))
                    .clickable(role = Role.Button, onClick = onActionClick)
                    .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Tiny),
                text = actionText,
                style = bookOnTypography.caption,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}
