package com.teamnative.bookon.core.ui.component.loading

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppAnimationDuration
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.BookOnColor
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme

/**
 * 전체 콘텐츠 영역 중앙에 일정한 속도로 회전하는 원호형 로딩 인디케이터를 표시한다.
 * 네트워크 요청이나 화면 상태를 직접 보유하지 않아 어느 Route에서나 재사용할 수 있다.
 */
@Composable
fun BookOnLoadingScreen(
    modifier: Modifier = Modifier,
) {
    val loadingDescription = stringResource(R.string.state_loading)
    val infiniteTransition = rememberInfiniteTransition(label = "BookOnLoadingRotation")
    val rotationDegrees = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppAnimationDuration.LoadingRotation,
                easing = LinearEasing,
            ),
        ),
        label = "BookOnLoadingRotationDegrees",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BookOnColor.Background)
            .semantics {
                contentDescription = loadingDescription
                stateDescription = loadingDescription
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(AppIconSize.Loading)) {
            drawArc(
                color = BookOnColor.Primary,
                startAngle = rotationDegrees.value,
                sweepAngle = LoadingArcSweepAngle,
                useCenter = false,
                style = Stroke(
                    width = LoadingArcStrokeWidth.toPx(),
                    cap = StrokeCap.Round,
                ),
            )
        }
    }
}

private const val LoadingArcSweepAngle = 270f
private val LoadingArcStrokeWidth = 8.dp

@Preview(showBackground = true)
@Composable
private fun BookOnLoadingScreenLightPreview() {
    BookOnTheme(darkTheme = false) {
        BookOnLoadingScreen()
    }
}

@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun BookOnLoadingScreenDarkPreview() {
    BookOnTheme(darkTheme = true) {
        BookOnLoadingScreen()
    }
}
