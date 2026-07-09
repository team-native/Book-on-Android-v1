package com.teamnative.bookon.ui.Component.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.book.BookOnBookDetailInfoItemUiState

/**
 * 상세 정보 행 내부의 단일 정보 타일이다.
 * highlighted는 대출 가능 여부처럼 주요 상태를 강조할 때 사용한다.
 */
@Composable
fun BookOnBookDetailInfoItem(
    item: BookOnBookDetailInfoItemUiState,
    modifier: Modifier = Modifier,
) {
    BookOnBookDetailInfoItem(
        label = item.label,
        value = item.value,
        modifier = modifier,
        highlighted = item.highlighted,
    )
}

/**
 * 상세 정보 행 내부의 단일 정보 타일이다.
 * highlighted는 대출 가능 여부처럼 주요 상태를 강조할 때 사용한다.
 */
@Composable
fun BookOnBookDetailInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
) {
    Column(
        modifier = modifier
            .height(68.dp)
            .clip(RoundedCornerShape(AppRadius.Small))
            .background(BookOnColor.SurfaceAlt)
            .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Item),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            style = BookOnTypography.caption,
            color = BookOnColor.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(AppSpacing.Tiny))
        Text(
            text = value,
            style = BookOnTypography.bodySemiBold,
            color = if (highlighted) BookOnColor.PrimaryPressed else BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}
