package com.example.bookon.ui.commonComponent.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookon.ui.commonComponent.bar.BookOnStepProgress
import com.example.bookon.theme.AppSpacing
import com.example.bookon.theme.BookOnColor
import com.example.bookon.theme.BookOnTheme
import com.example.bookon.theme.BookOnTypography

private const val SignupTotalStep = 3

/**
 * 회원가입 단계 상단에서 진행률, 제목, 설명을 같은 규칙으로 표시한다.
 * step은 현재 단계이며 전체 단계 수는 회원가입 플로우 기준으로 고정한다.
 */
@Composable
fun BookOnSignupStepHeader(
    step: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
    ) {
        BookOnStepProgress(currentStep = step, totalStep = SignupTotalStep)
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
            Text(
                text = title,
                style = BookOnTypography.screenTitle,
                color = BookOnColor.TextPrimary,
            )
            Text(
                text = description,
                style = BookOnTypography.bodyMedium,
                color = BookOnColor.TextSecondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnSignupStepHeaderPreview() {
    BookOnTheme {
        BookOnSignupStepHeader(
            step = 1,
            title = "학교 정보",
            description = "학교 계정으로 간편하게 가입하세요!",
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
