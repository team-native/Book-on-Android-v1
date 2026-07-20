package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

private val SignupSummaryCardWidth = 141.dp
private val SignupSummaryCardHeight = 64.dp
private val SignupSummaryDividerHeight = 40.dp
private val SignupSummaryBorderWidth = 1.2.dp
private val SignupSummaryDividerWidth = 1.dp
private val SignupSummaryTextSpacing = 1.dp

/**
 * 가입 완료 화면의 보유 도서와 독서마라톤 상태를 Figma 시안의 단일 요약 카드로 표시한다.
 * 표시 문자열은 호출 화면에서 resource 또는 UiState로 구성해 전달한다.
 */
@Composable
fun BookOnSignupCompleteSummary(
    ownedBookCountText: String,
    ownedBookDescription: String,
    marathonStatusText: String,
    marathonDescription: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .width(SignupSummaryCardWidth)
            .height(SignupSummaryCardHeight),
        shape = RoundedCornerShape(AppRadius.Chip),
        color = BookOnColor.SurfaceAlt,
        border = BorderStroke(SignupSummaryBorderWidth, BookOnColor.SurfaceBorder),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            SignupSummaryItem(
                title = ownedBookCountText,
                description = ownedBookDescription,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .height(SignupSummaryDividerHeight)
                    .width(SignupSummaryDividerWidth)
                    .align(Alignment.CenterVertically)
                    .background(BookOnColor.SwitchOff),
            )
            SignupSummaryItem(
                title = marathonStatusText,
                description = marathonDescription,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** 요약 카드 한 칸의 상태값과 설명을 가운데 정렬해 표시한다. */
@Composable
private fun SignupSummaryItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        Text(
            text = title,
            style = BookOnTypography.bodySemiBold,
            color = BookOnColor.PrimaryPressed,
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(SignupSummaryTextSpacing))
        Text(
            text = description,
            style = BookOnTypography.privacyNotice,
            color = BookOnColor.TextTertiary,
        )
    }
}
