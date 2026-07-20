package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.BookOnColor

private val SignupCompleteHeroHeight = 276.dp
private val SignupCompleteIconSize = 100.dp
private val SignupCompleteCheckCanvasSize = 44.dp
private val SignupCompleteCheckStrokeWidth = 9.dp
private val SignupCompleteCheckStartX = 4.dp
private val SignupCompleteCheckStartY = 23.dp
private val SignupCompleteCheckMiddleX = 17.dp
private val SignupCompleteCheckMiddleY = 36.dp
private val SignupCompleteCheckEndX = 40.dp
private val SignupCompleteCheckEndY = 9.dp

/** 가입 완료 화면 상단의 그라데이션 배경과 완료 마크를 각각 제공한다. */
@Composable
fun BookOnSignupCompleteHeroBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SignupCompleteHeroHeight)
            .background(
                Brush.verticalGradient(
                    colors = listOf(BookOnColor.PrimaryLight, BookOnColor.Background),
                ),
            ),
    )
}

/** 가입 성공을 알리는 녹색 원형 체크 마크를 표시한다. */
@Composable
fun BookOnSignupCompleteCheckIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(SignupCompleteIconSize)
            .shadow(AppElevation.Card, CircleShape)
            .clip(CircleShape)
            .background(BookOnColor.Primary),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(SignupCompleteCheckCanvasSize)) {
            val strokeWidth = SignupCompleteCheckStrokeWidth.toPx()
            drawLine(
                color = BookOnColor.Surface,
                start = androidx.compose.ui.geometry.Offset(
                    SignupCompleteCheckStartX.toPx(),
                    SignupCompleteCheckStartY.toPx(),
                ),
                end = androidx.compose.ui.geometry.Offset(
                    SignupCompleteCheckMiddleX.toPx(),
                    SignupCompleteCheckMiddleY.toPx(),
                ),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = BookOnColor.Surface,
                start = androidx.compose.ui.geometry.Offset(
                    SignupCompleteCheckMiddleX.toPx(),
                    SignupCompleteCheckMiddleY.toPx(),
                ),
                end = androidx.compose.ui.geometry.Offset(
                    SignupCompleteCheckEndX.toPx(),
                    SignupCompleteCheckEndY.toPx(),
                ),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
        }
    }
}
