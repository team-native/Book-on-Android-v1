package com.teamnative.bookon.ui.Component.bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.theme.AppRadius
import com.teamnative.bookon.theme.AppSpacing
import com.teamnative.bookon.theme.BookOnColor
import com.teamnative.bookon.theme.BookOnTheme
import com.teamnative.bookon.theme.BookOnTypography
import com.teamnative.bookon.uiState.BookOnStepProgressUiState

private const val StepProgressSegmentAnimationMillis = 720
private const val StepProgressSegmentDelayMillis = 240

/**
 * 회원가입처럼 순차 단계가 있는 화면에서 현재 진행 상태를 표시한다.
 * currentStep은 1부터 시작하며 totalStep보다 크면 마지막 단계로 표시한다.
 */
@Composable
fun BookOnStepProgress(
    uiState: BookOnStepProgressUiState,
    modifier: Modifier = Modifier,
    animateProgress: Boolean = true,
    onAnimationRunningChange: (Boolean) -> Unit = {},
) {
    BookOnStepProgress(
        currentStep = uiState.currentStep,
        totalStep = uiState.totalStep,
        modifier = modifier,
        animateProgress = animateProgress,
        onAnimationRunningChange = onAnimationRunningChange,
    )
}

/**
 * 회원가입처럼 순차 단계가 있는 화면에서 현재 진행 상태를 표시한다.
 * currentStep은 1부터 시작하며 현재 단계까지의 블록을 순차적으로 쌓아 표시한다.
 */
@Composable
fun BookOnStepProgress(
    currentStep: Int,
    totalStep: Int,
    modifier: Modifier = Modifier,
    animateProgress: Boolean = true,
    onAnimationRunningChange: (Boolean) -> Unit = {},
) {
    val safeTotal = totalStep.coerceAtLeast(1)
    val safeCurrent = currentStep.coerceIn(1, safeTotal)
    val initialStackedStep = if (animateProgress) {
        (safeCurrent - 1).coerceAtLeast(1)
    } else {
        safeCurrent
    }
    val segmentProgresses = remember(safeTotal) {
        List(safeTotal) { index ->
            Animatable(if (index < initialStackedStep) 1f else 0f)
        }
    }
    var stackedStep by remember(safeTotal) { mutableIntStateOf(initialStackedStep) }
    val currentAnimationRunningChange by rememberUpdatedState(onAnimationRunningChange)

    LaunchedEffect(safeCurrent, safeTotal, animateProgress) {
        if (!animateProgress || safeCurrent == stackedStep) {
            currentAnimationRunningChange(false)
            return@LaunchedEffect
        }

        currentAnimationRunningChange(true)
        try {
            if (safeCurrent < stackedStep) {
                segmentProgresses.forEachIndexed { index, progress ->
                    progress.snapTo(if (index < safeCurrent) 1f else 0f)
                }
                stackedStep = safeCurrent
                return@LaunchedEffect
            }

            repeat(safeCurrent - stackedStep) { offset ->
                val index = stackedStep + offset
                stackedStep = index + 1
                segmentProgresses[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = StepProgressSegmentAnimationMillis,
                        delayMillis = if (offset == 0) 0 else StepProgressSegmentDelayMillis,
                    ),
                )
            }
        } finally {
            currentAnimationRunningChange(false)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small),
        ) {
            repeat(safeTotal) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(AppRadius.Progress))
                        .background(BookOnColor.SurfaceBorder),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(segmentProgresses[index].value)
                            .height(4.dp)
                            .background(BookOnColor.Primary),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        Text(
            text = "STEP $stackedStep / $safeTotal",
            style = BookOnTypography.caption,
            color = BookOnColor.TextTertiary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnStepProgressPreview() {
    BookOnTheme {
        BookOnStepProgress(
            currentStep = 2,
            totalStep = 3,
        )
    }
}
