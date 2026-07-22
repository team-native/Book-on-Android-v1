package com.teamnative.bookon.feature.library.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

/** 도서실 제목과 인기순·신간순 정렬 토글을 함께 표시한다. */
@Composable
fun BookOnLibraryHeader(
    title: String,
    sortOptions: List<BookOnFilterChipUiModel>,
    onSortClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = BookOnTypography.screenTitle, color = BookOnColor.TextPrimary)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .width(AppComponentSize.LibrarySortToggleWidth)
                .height(AppComponentSize.LibrarySortToggleHeight)
                .clip(RoundedCornerShape(AppRadius.Button))
                .background(BookOnColor.SurfaceAlt)
                .padding(AppSpacing.Tiny),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Tiny),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            sortOptions.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(AppComponentSize.LibrarySortOptionHeight)
                        .clip(RoundedCornerShape(AppRadius.Button))
                        .then(
                            if (option.selected) {
                                Modifier
                                    .shadow(AppElevation.Field, RoundedCornerShape(AppRadius.Button))
                                    .background(BookOnColor.Surface)
                            } else {
                                Modifier.background(BookOnColor.SurfaceAlt)
                            },
                        )
                        .clickable(role = Role.Button, onClick = { onSortClick(index) })
                        .padding(horizontal = AppSpacing.Small),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option.text,
                        style = BookOnTypography.caption,
                        color = if (option.selected) BookOnColor.TextPrimary else BookOnColor.TextSecondary,
                    )
                }
            }
        }
    }
}
