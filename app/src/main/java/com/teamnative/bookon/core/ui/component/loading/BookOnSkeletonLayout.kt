package com.teamnative.bookon.core.ui.component.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing

/** 데이터 영역의 장식용 도형을 하나의 로딩 상태로 안내한다. */
@Composable
fun BookOnSkeletonLayout(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = AppSpacing.ScreenHorizontal,
    content: @Composable ColumnScope.() -> Unit,
) {
    val loadingDescription = stringResource(R.string.state_loading)
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics {
                contentDescription = loadingDescription
                stateDescription = loadingDescription
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            }
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = horizontalPadding,
                vertical = AppSpacing.Content,
            ),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        content = content,
    )
}
