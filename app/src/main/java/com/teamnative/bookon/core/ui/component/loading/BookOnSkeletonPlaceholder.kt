package com.teamnative.bookon.core.ui.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnThemeMode
import androidx.compose.foundation.layout.size
import com.teamnative.bookon.core.designsystem.theme.AppIconSize

@Composable
fun BookOnSkeletonPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(AppRadius.Small),
) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = shape,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun BookOnSkeletonPlaceholderLightPreview() {
    BookOnTheme(themeMode = BookOnThemeMode.LIGHT) {
        BookOnSkeletonPlaceholder(Modifier.size(AppIconSize.Avatar))
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSkeletonPlaceholderDarkPreview() {
    BookOnTheme(themeMode = BookOnThemeMode.DARK) {
        BookOnSkeletonPlaceholder(Modifier.size(AppIconSize.Avatar))
    }
}
