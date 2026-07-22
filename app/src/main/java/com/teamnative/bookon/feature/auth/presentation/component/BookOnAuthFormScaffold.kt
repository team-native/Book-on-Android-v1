package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnColor

/**
 * 회원가입 계열 화면에서 공통으로 쓰는 Scaffold, 상단바, 하단 액션 배치를 제공한다.
 * 각 단계 Screen은 content와 footer slot만 넘겨 화면별 입력 흐름을 조립한다.
 */
@Composable
fun BookOnAuthFormScaffold(
    topBar: @Composable () -> Unit,
    footer: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = BookOnColor.Background,
        topBar = {
            Column(modifier = Modifier.padding(horizontal = AppSpacing.AuthHorizontal)) {
                topBar()
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.AuthHorizontal)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
            ) { content() }
            Column(
                modifier = Modifier.padding(horizontal = AppSpacing.Small),
                content = footer,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Section))

        }
    }
}
