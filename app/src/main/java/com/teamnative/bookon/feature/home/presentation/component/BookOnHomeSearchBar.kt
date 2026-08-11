package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

/**
 * 홈 메인에서 검색 화면으로 이동하는 검색형 버튼이다.
 * 실제 텍스트 입력은 검색 Route에서 담당하고, 홈에서는 명확한 탐색 액션만 제공한다.
 */
@Composable
fun BookOnHomeSearchBar(
    placeholder: String,
    searchContentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.HomeSearchHeight)
            .shadow(
                elevation = AppElevation.Field,
                shape = RoundedCornerShape(AppRadius.Search),
            )
            .clip(RoundedCornerShape(AppRadius.Search))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = placeholder,
            style = bookOnTypography.fieldPlaceholder,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Image(
            modifier = Modifier.size(AppIconSize.Small),
            painter = painterResource(R.drawable.main_search),
            contentDescription = searchContentDescription,
            contentScale = ContentScale.Fit,
        )
    }
}
