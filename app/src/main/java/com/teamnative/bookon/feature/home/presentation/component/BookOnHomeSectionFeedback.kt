package com.teamnative.bookon.feature.home.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.designsystem.theme.AppSpacing

/** 홈의 한 섹션이 로딩 또는 오류일 때, 해당 섹션 안에서만 상태와 재시도 동작을 표시한다. */
@Composable
fun BookOnHomeSectionFeedback(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    retryText: String? = null,
    onRetryClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        if (title != null) {
            Text(text = title, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        if (message == null) {
            CircularProgressIndicator()
        } else {
            Text(text = message, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (retryText != null && onRetryClick != null) {
                Button(onClick = onRetryClick) {
                    Text(text = retryText)
                }
            }
        }
    }
}
