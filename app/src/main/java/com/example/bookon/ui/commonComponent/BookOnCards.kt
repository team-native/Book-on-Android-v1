package com.example.bookon.ui.commonComponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookon.ui.theme.AppComponentSize
import com.example.bookon.ui.theme.AppElevation
import com.example.bookon.ui.theme.AppIconSize
import com.example.bookon.ui.theme.AppRadius
import com.example.bookon.ui.theme.AppSpacing
import com.example.bookon.ui.theme.BookOnColor
import com.example.bookon.ui.theme.BookOnTheme
import com.example.bookon.ui.theme.BookOnTypography
import com.example.bookon.uiState.BookOnBookCardUiState
import com.example.bookon.uiState.BookOnFilterChipUiState
import com.example.bookon.uiState.BookOnInfoCardUiState
import com.example.bookon.uiState.BookOnMenuRowUiState
import com.example.bookon.uiState.BookOnStatSummaryCardUiState
import com.example.bookon.uiState.BookOnSwitchRowUiState

@Immutable
data class BookOnStatItem(
    val label: String,
    val value: String,
)

/**
 * 마이페이지의 대출 현황처럼 2~3개 수치를 한 줄 카드로 표시한다.
 * 값은 호출 측에서 이미 화면 문자열로 만든 뒤 전달한다.
 */
@Composable
fun BookOnStatSummaryCard(
    uiState: BookOnStatSummaryCardUiState,
    modifier: Modifier = Modifier,
) {
    BookOnStatSummaryCard(
        items = uiState.items,
        modifier = modifier,
    )
}

/**
 * 마이페이지의 대출 현황처럼 2~3개 수치를 한 줄 카드로 표시한다.
 * 값은 호출 측에서 이미 화면 문자열로 만든 뒤 전달한다.
 */
@Composable
fun BookOnStatSummaryCard(
    items: List<BookOnStatItem>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .shadow(
                elevation = AppElevation.StrongCard,
                shape = RoundedCornerShape(AppRadius.Card),
            )
            .clip(RoundedCornerShape(AppRadius.Card))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(BookOnColor.Primary, BookOnColor.PrimaryDark),
                ),
            )
            .padding(vertical = AppSpacing.Content),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = item.label,
                    style = BookOnTypography.caption,
                    color = BookOnColor.PrimaryLight,
                )
                Spacer(modifier = Modifier.height(AppSpacing.Tiny))
                Text(
                    text = item.value,
                    style = BookOnTypography.sectionTitle,
                    color = BookOnColor.PrimaryLight,
                )
            }

            if (index < items.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(BookOnColor.PrimaryLight.copy(alpha = 0.45f)),
                )
            }
        }
    }
}

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
                    .size(36.dp)
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

/**
 * 마이페이지 설정 목록처럼 제목, 우측 액션, 하단 구분선을 가진 행이다.
 * onClick이 null이면 읽기 전용 행으로 표시한다.
 */
@Composable
fun BookOnMenuRow(
    uiState: BookOnMenuRowUiState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit = { MenuChevron() },
) {
    BookOnMenuRow(
        title = uiState.title,
        modifier = modifier,
        onClick = onClick,
        destructive = uiState.destructive,
        showDivider = uiState.showDivider,
        trailingContent = trailingContent,
    )
}

/**
 * 마이페이지 설정 목록처럼 제목, 우측 액션, 하단 구분선을 가진 행이다.
 * onClick이 null이면 읽기 전용 행으로 표시한다.
 */
@Composable
fun BookOnMenuRow(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    destructive: Boolean = false,
    showDivider: Boolean = true,
    trailingContent: @Composable () -> Unit = { MenuChevron() },
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(role = Role.Button, onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = BookOnTypography.bodySemiBold,
                color = if (destructive) BookOnColor.Error else BookOnColor.TextPrimary,
            )
            trailingContent()
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BookOnColor.Divider),
            )
        }
    }
}

/**
 * 검색 필터와 내역 상태 필터에 쓰는 선택형 칩이다.
 * 선택 상태는 배경색과 텍스트 색상만으로 구분되지 않도록 selected 값을 semantics에 연결한다.
 */
@Composable
fun BookOnFilterChip(
    uiState: BookOnFilterChipUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnFilterChip(
        text = uiState.text,
        selected = uiState.selected,
        onClick = onClick,
        modifier = modifier,
    )
}

/**
 * 검색 필터와 내역 상태 필터에 쓰는 선택형 칩이다.
 * 선택 상태는 배경색과 텍스트 색상만으로 구분되지 않도록 selected 값을 semantics에 연결한다.
 */
@Composable
fun BookOnFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppComponentSize.ChipHeight)
            .clip(RoundedCornerShape(AppRadius.Chip))
            .background(if (selected) BookOnColor.PrimaryPressed else BookOnColor.SurfaceAlt)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = AppSpacing.Content),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = BookOnTypography.chip,
            color = if (selected) BookOnColor.Surface else BookOnColor.TextDarkGray,
        )
    }
}

/**
 * 제목과 설명, Switch를 한 행으로 묶어 알림/연동 설정에 사용한다.
 */
@Composable
fun BookOnSwitchRow(
    uiState: BookOnSwitchRowUiState,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookOnSwitchRow(
        title = uiState.title,
        checked = uiState.checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        description = uiState.description,
    )
}

/**
 * 제목과 설명, Switch를 한 행으로 묶어 알림/연동 설정에 사용한다.
 */
@Composable
fun BookOnSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BookOnColor.Surface,
                checkedTrackColor = BookOnColor.Primary,
                uncheckedThumbColor = BookOnColor.Surface,
                uncheckedTrackColor = BookOnColor.SwitchOff,
                uncheckedBorderColor = BookOnColor.SwitchOff,
            ),
        )
    }
}

/**
 * 책 표지와 제목/저자 정보를 표시하는 공통 카드이다.
 * cover는 이미지 로더나 로컬 drawable을 연결할 수 있도록 slot으로 받는다.
 */
@Composable
fun BookOnBookCard(
    uiState: BookOnBookCardUiState,
    modifier: Modifier = Modifier,
    cover: @Composable () -> Unit = { BookCoverPlaceholder() },
    titleStyle: TextStyle = BookOnTypography.bookTitle,
) {
    BookOnBookCard(
        title = uiState.title,
        author = uiState.author,
        modifier = modifier,
        cover = cover,
        titleStyle = titleStyle,
    )
}

/**
 * 책 표지와 제목/저자 정보를 표시하는 공통 카드이다.
 * cover는 이미지 로더나 로컬 drawable을 연결할 수 있도록 slot으로 받는다.
 */
@Composable
fun BookOnBookCard(
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    cover: @Composable () -> Unit = { BookCoverPlaceholder() },
    titleStyle: TextStyle = BookOnTypography.bookTitle,
) {
    Column(modifier = modifier.width(AppComponentSize.BookCoverWidth)) {
        Box(
            modifier = Modifier
                .size(
                    width = AppComponentSize.BookCoverWidth,
                    height = AppComponentSize.BookCoverHeight,
                )
                .shadow(
                    elevation = AppElevation.BookCover,
                    shape = RoundedCornerShape(AppRadius.Small),
                )
                .clip(RoundedCornerShape(AppRadius.Small)),
            contentAlignment = Alignment.Center,
        ) {
            cover()
        }
        Spacer(modifier = Modifier.height(AppSpacing.Item))
        Text(
            text = title,
            style = titleStyle,
            color = BookOnColor.TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = author,
            style = BookOnTypography.bookMeta,
            color = BookOnColor.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun BookCoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppComponentSize.BookCoverHeight)
            .background(BookOnColor.BookCoverPlaceholder),
    )
}

@Composable
private fun MenuChevron() {
    Canvas(modifier = Modifier.size(AppIconSize.Small)) {
        val strokeWidth = 1.5.dp.toPx()
        val startX = size.width * 0.35f
        val endX = size.width * 0.65f
        drawLine(
            color = BookOnColor.TextPlaceholder,
            start = Offset(startX, size.height * 0.2f),
            end = Offset(endX, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = BookOnColor.TextPlaceholder,
            start = Offset(endX, size.height * 0.5f),
            end = Offset(startX, size.height * 0.8f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnStatSummaryCardPreview() {
    BookOnTheme {
        BookOnStatSummaryCard(
            items = listOf(
                BookOnStatItem("대출 중", "3권"),
                BookOnStatItem("반납 임박", "2권"),
                BookOnStatItem("누적 대출", "23권"),
            ),
        )
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

@Preview(showBackground = true)
@Composable
private fun BookOnMenuRowPreview() {
    BookOnTheme {
        BookOnMenuRow(
            title = "대출 / 반납 내역",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnFilterChipPreview() {
    BookOnTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
            BookOnFilterChip(text = "대출 중 2", selected = false, onClick = {})
            BookOnFilterChip(text = "반납 완료", selected = true, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSwitchRowPreview() {
    BookOnTheme {
        BookOnSwitchRow(
            title = "반납 알림",
            description = "반납 3일 전과 당일에 알려드려요",
            checked = true,
            onCheckedChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnBookCardPreview() {
    BookOnTheme {
        BookOnBookCard(
            title = "자몽 살구 클럽",
            author = "한로로",
        )
    }
}
