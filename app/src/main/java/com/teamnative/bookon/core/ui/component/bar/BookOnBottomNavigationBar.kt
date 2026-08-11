package com.teamnative.bookon.core.ui.component.bar

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.LocalBookOnExtraColors
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnNavigationItemUiModel

/**
 * 앱 하단 주요 목적지 4개를 표시하는 공통 내비게이션 바이다.
 * 이미지 리소스 아이콘은 선택 상태에 따라 앱 주요 색상 또는 비활성 색상으로 표시된다.
 */
@Composable
fun BookOnBottomNavigationBar(
    items: List<BookOnNavigationItemUiModel>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.NavigationHeight),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppStrokeWidth.Divider)
                    .background(LocalBookOnExtraColors.current.navigationDivider),
            )

            Spacer(modifier = Modifier.height(AppSpacing.NavigationDividerToIcon))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                items.forEachIndexed { index, item ->
                    val selected = index == selectedIndex
                    val labelColor = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                    val label = stringResource(item.labelRes)
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppRadius.Small))
                            .selectable(
                                selected = selected,
                                role = Role.Tab,
                                onClick = { onItemClick(index) },
                            )
                            .padding(horizontal = AppSpacing.Small),
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
                                colorFilter = if (selected) {
                                    ColorFilter.tint(
                                        MaterialTheme.colorScheme.primary,
                                    )
                                } else {
                                    null
                                },
                            )
                        }
                        Text(
                            text = label,
                            style = bookOnTypography.bookMeta,
                            color = labelColor,
                        )
                    }
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
            items = listOf(
                BookOnNavigationItemUiModel(R.string.nav_home, R.drawable.navigation_home),
                BookOnNavigationItemUiModel(R.string.nav_ranking, R.drawable.navigation_rank),
                BookOnNavigationItemUiModel(R.string.nav_library, R.drawable.navigation_library),
                BookOnNavigationItemUiModel(R.string.nav_my, R.drawable.navigation_my),
            ),
            selectedIndex = 0,
            onItemClick = {},
        )
    }
}
