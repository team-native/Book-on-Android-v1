package com.teamnative.bookon.core.ui.component.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor

/** 목록의 다음 페이지 요청처럼 기존 콘텐츠를 유지해야 할 때 표시하는 진행 상태다. */
@Composable
fun BookOnInlineLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    val loadingDescription = stringResource(R.string.state_loading)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.Content)
            .semantics {
                contentDescription = loadingDescription
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            color = BookOnColor.Primary,
            modifier = Modifier,
        )
    }
}
