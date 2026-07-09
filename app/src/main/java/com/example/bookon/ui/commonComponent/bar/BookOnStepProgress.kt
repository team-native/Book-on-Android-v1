package com.example.bookon.ui.commonComponent.bar

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookon.theme.AppRadius
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography
import com.example.bookon.uiState.BookOnStepProgressUiState

/**
 * 회원가입처럼 순차 단계가 있는 화면에서 현재 진행 상태를 표시한다.
 * currentStep은 1부터 시작하며 totalStep보다 크면 마지막 단계로 표시한다.
 */
@Composable
fun BookOnStepProgress(
    uiState: BookOnStepProgressUiState,
    modifier: Modifier = Modifier,
) {
    BookOnStepProgress(
        currentStep = uiState.currentStep,
        totalStep = uiState.totalStep,
        modifier = modifier,
    )
}

/**
 * 회원가입처럼 순차 단계가 있는 화면에서 현재 진행 상태를 표시한다.
 * currentStep은 1부터 시작하며 totalStep보다 크면 마지막 단계로 표시한다.
 */
@Composable
fun BookOnStepProgress(
    currentStep: Int,
    totalStep: Int,
    modifier: Modifier = Modifier,
) {
    val safeTotal = totalStep.coerceAtLeast(1)
    val safeCurrent = currentStep.coerceIn(1, safeTotal)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small),
        ) {
            repeat(safeTotal) { index ->
                val selected = index < safeCurrent
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(AppRadius.Progress))
                        .background(if (selected) BookOnColor.Primary else BookOnColor.SurfaceBorder),
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        Text(
            text = "STEP $safeCurrent / $safeTotal",
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
