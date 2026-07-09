package com.teamnative.bookon.ui.Component.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppComponentSize
import com.teamnative.bookon.theme.AppIconSize
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.navigation.BookOnNavigationItem
import com.teamnative.bookon.navigation.defaultBookOnNavigationItems
import com.teamnative.bookon.uiState.BookOnBottomNavigationUiState

/**
 * 앱 하단 주요 목적지 4개를 표시하는 공통 내비게이션 바이다.
 * 이미지 리소스 아이콘은 선택 상태에 따라 앱 주요 색상 또는 비활성 색상으로 표시된다.
 */
@Composable
fun BookOnBottomNavigationBar(
    uiState: BookOnBottomNavigationUiState,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnBottomNavigationBar(
        items = uiState.items,
        selectedIndex = uiState.selectedIndex,
        onItemClick = onItemClick,
        modifier = modifier,
    )
}

/**
 * 앱 하단 주요 목적지 4개를 표시하는 공통 내비게이션 바이다.
 * 이미지 리소스 아이콘은 선택 상태에 따라 앱 주요 색상 또는 비활성 색상으로 표시된다.
 */
@Composable
fun BookOnBottomNavigationBar(
    items: List<BookOnNavigationItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.NavigationHeight),
        color = BookOnColor.Surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.ScreenHorizontal),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                val itemColor = if (selected) BookOnColor.Primary else BookOnColor.NavigationInactive
                val label = stringResource(item.labelRes)
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(AppRadius.Small))
                        .selectable(
                            selected = selected,
                            role = Role.Tab,
                            onClick = { onItemClick(index) },
                        )
                        .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Tiny),
                ) {
                    Box(
                        modifier = Modifier.size(AppIconSize.Default),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(itemColor),
                        )
                    }
                    Text(
                        text = label,
                        style = BookOnTypography.bookMeta,
                        color = itemColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBottomNavigationBarPreview() {
    BookOnTheme {
        BookOnBottomNavigationBar(
            items = defaultBookOnNavigationItems(),
            selectedIndex = 0,
            onItemClick = {},
        )
    }
}
