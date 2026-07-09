package com.example.bookon.ui.commonComponent.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.theme.AppComponentSize
import com.example.bookon.theme.AppIconSize
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.BookOnInfoCardUiState

/**
 * 안내, 연동, 공지 등 아이콘과 설명이 함께 있는 공통 카드이다.
 * leadingContent와 trailingContent는 화면별 아이콘이나 토글을 연결할 때 사용한다.
 */
@Composable
fun BookOnInfoCard(
    uiState: BookOnInfoCardUiState,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    BookOnInfoCard(
        title = uiState.title,
        modifier = modifier,
        description = uiState.description,
        containerColor = uiState.containerColor,
        borderColor = uiState.borderColor,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
    )
}

/**
 * 안내, 연동, 공지 등 아이콘과 설명이 함께 있는 공통 카드이다.
 * leadingContent와 trailingContent는 화면별 아이콘이나 토글을 연결할 때 사용한다.
 */
@Composable
fun BookOnInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    containerColor: Color = BookOnColor.Background,
    borderColor: Color = BookOnColor.SurfaceBorder,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.Card))
            .background(containerColor)
            .padding(AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingContent != null) {
            Box(
                modifier = Modifier
                    .size(AppComponentSize.InfoCardIcon)
                    .clip(RoundedCornerShape(AppRadius.IconButton))
                    .background(borderColor),
                contentAlignment = Alignment.Center,
            ) {
                leadingContent()
            }
            Spacer(modifier = Modifier.width(AppSpacing.Content))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = BookOnTypography.bodySemiBold,
                color = BookOnColor.TextPrimary,
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(AppSpacing.Tiny))
                Text(
                    text = description,
                    style = BookOnTypography.caption,
                    color = BookOnColor.TextSecondary,
                )
            }
        }

        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(AppSpacing.Content))
            trailingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnInfoCardPreview() {
    BookOnTheme {
        BookOnInfoCard(
            title = "2026 독서마라톤",
            description = "아직 연동하지 않았어요",
            leadingContent = {
                Box(
                    modifier = Modifier
                        .size(AppIconSize.Small)
                        .background(BookOnColor.Primary),
                )
            },
        )
    }
}
