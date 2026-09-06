package com.teamnative.bookon.feature.my.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.component.bar.BookOnTopBar
import com.teamnative.bookon.core.ui.component.card.BookOnStatSummaryCard
import com.teamnative.bookon.core.ui.component.row.BookOnMenuRow
import com.teamnative.bookon.core.ui.model.resolve
import com.teamnative.bookon.feature.my.presentation.component.BookOnLogoutButton
import com.teamnative.bookon.feature.my.presentation.component.BookOnMyMarathonCard
import com.teamnative.bookon.feature.my.presentation.component.BookOnMyProfileHeader
import com.teamnative.bookon.feature.my.presentation.component.BookOnMyUnlinkedMarathonCard

/** 프로필, 통계, 독서마라톤, 메뉴와 알림 설정을 표시한다. */
@Composable
fun BookOnMyScreen(
    uiState: BookOnMyScreenUiState,
    bottomBar: @Composable () -> Unit,
    onEvent: (BookOnMyScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BookOnTopBar(
                title = stringResource(R.string.my_library_title),
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Content),
        ) {
            uiState.errorMessage?.let { message ->
                item {
                    Text(
                        text = message.resolve(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(onClick = { onEvent(BookOnMyScreenEvent.RetryClicked) }) {
                        Text(text = stringResource(R.string.action_retry))
                    }
                }
            }
            // 프로필 조회가 실패해도 프로필/통계/메뉴/로그아웃 등 이미 만들어둔 UI는 기본 형식으로 항상 노출한다.
            item {
                BookOnMyProfileHeader(
                    userNameText = uiState.userNameText,
                    studentInfoText = uiState.studentInfoText,
                    profileImageUrl = uiState.profileImageUrl,
                    isProfileImageUploading = uiState.isProfileImageUploading,
                    onProfileImageEditClick = {
                        onEvent(BookOnMyScreenEvent.ProfileImageEditClicked)
                    },
                )
            }

            uiState.profileImageErrorMessage?.let { message ->
                item {
                    Text(
                        text = message.resolve(),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            item { BookOnStatSummaryCard(items = uiState.stats) }
            item {
                if (uiState.marathon.linked) {
                    BookOnMyMarathonCard(uiState = uiState.marathon)
                } else {
                    BookOnMyUnlinkedMarathonCard(
                        uiState = uiState.marathon,
                        onLinkRequest = {
                            onEvent(BookOnMyScreenEvent.ReadingMarathonLinkRequested)
                        },
                    )
                }
            }
            item {
                Column {
                    uiState.menus.forEachIndexed { index, menu ->
                        BookOnMenuRow(
                            title = menu.title,
                            onClick = {
                                onEvent(BookOnMyScreenEvent.MenuClicked(index))
                            },
                            destructive = menu.destructive,
                            showDivider = menu.showDivider,
                        )
                    }
                }
            }
            item {
                BookOnLogoutButton(
                    onLogoutRequest = {
                        onEvent(BookOnMyScreenEvent.LogoutClicked)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMyScreenPreview() {
    BookOnTheme {
        BookOnMyScreen(
            uiState = defaultMyUiState(),
            bottomBar = {},
            onEvent = {},
        )
    }
}
