package com.teamnative.bookon.core.ui.component.loading

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnThemeMode

/**
 * 전체 콘텐츠 영역 중앙에 Material 기본 로딩 인디케이터를 표시한다.
 * 네트워크 요청이나 화면 상태를 직접 보유하지 않아 어느 Route에서나 재사용할 수 있다.
 */
@Composable
fun BookOnLoadingScreen(
    modifier: Modifier = Modifier,
) {
    val loadingDescription = stringResource(R.string.state_loading)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .semantics {
                contentDescription = loadingDescription
                stateDescription = loadingDescription
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnLoadingScreenLightPreview() {
    BookOnTheme(themeMode = BookOnThemeMode.LIGHT) {
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
    BookOnTheme(themeMode = BookOnThemeMode.DARK) {
        BookOnLoadingScreen()
    }
}
