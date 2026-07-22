package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.AppStrokeWidth
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTypography

private const val PasswordResetTotalStep = 3

/** 비밀번호 재설정 단계 비율만큼 하나의 트랙을 채워 진행 상태를 표시한다. */
@Composable
internal fun BookOnPasswordResetProgress(
    currentStep: BookOnPasswordResetStep,
    modifier: Modifier = Modifier,
) {
    val progress = currentStep.index.toFloat() / PasswordResetTotalStep

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppStrokeWidth.Progress)
                .clip(RoundedCornerShape(AppRadius.Progress))
                .background(BookOnColor.SurfaceBorder),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(AppStrokeWidth.Progress)
                    .background(BookOnColor.Primary),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = stringResource(R.string.password_reset_step, currentStep.index, PasswordResetTotalStep),
            style = BookOnTypography.caption,
            color = BookOnColor.TextTertiary,
        )
    }
}
