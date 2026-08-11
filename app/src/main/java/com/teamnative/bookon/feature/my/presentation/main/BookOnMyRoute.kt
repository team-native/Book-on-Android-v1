package com.teamnative.bookon.feature.my.presentation.main

import androidx.compose.material3.MaterialTheme

import android.app.AlertDialog
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.feature.my.presentation.component.BookOnNotificationSettingsBottomSheetContent

private const val PasswordChangeMenuIndex = 0
private const val LoanHistoryMenuIndex = 1
private const val FavoriteMenuIndex = 2
private const val NotificationSettingsMenuIndex = 3
private val InitialNotificationSelections = listOf(false, false)

/** 내 서재 샘플 상태와 메뉴·로그아웃 이벤트를 연결한다. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookOnMyRoute(
    bottomBar: @Composable () -> Unit,
    viewModel: BookOnMyViewModel = hiltViewModel(),
    onPasswordChangeClick: () -> Unit,
    onLoanHistoryClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onReadingMarathonLinkClick: () -> Unit,
    onLogoutRequest: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var isLogoutDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isNotificationSettingsVisible by rememberSaveable { mutableStateOf(false) }
    var notificationSelections by rememberSaveable { mutableStateOf(InitialNotificationSelections) }

    if (uiState.isInitialLoading) {
        BookOnLoadingScreen()
        return
    }

    BookOnMyScreen(
        uiState = uiState.copy(
            marathon = uiState.marathon.copy(
                statusText = if (uiState.isReadingMarathonLinked) uiState.marathon.statusText else "",
                progressText = if (uiState.isReadingMarathonLinked) {
                    uiState.marathon.progressText
                } else {
                    stringResource(R.string.reading_marathon_not_linked)
                },
                remainingText = if (uiState.isReadingMarathonLinked) {
                    uiState.marathon.remainingText
                } else {
                    stringResource(R.string.reading_marathon_link_toggle_description)
                },
                percentText = if (uiState.isReadingMarathonLinked) uiState.marathon.percentText else "",
                linked = uiState.isReadingMarathonLinked,
                progress = if (uiState.isReadingMarathonLinked) uiState.marathon.progress else 0f,
            ),
        ),
        bottomBar = bottomBar,
        modifier = if (isLogoutDialogVisible) Modifier.blur(radius = 8.dp) else Modifier,
        onEvent = { event ->
            when (event) {
                is BookOnMyScreenEvent.MenuClicked -> {
                    when (event.menuIndex) {
                        PasswordChangeMenuIndex -> onPasswordChangeClick()
                        LoanHistoryMenuIndex -> onLoanHistoryClick()
                        FavoriteMenuIndex -> onFavoriteClick()
                        NotificationSettingsMenuIndex -> {
                            notificationSelections = listOf(
                                uiState.notificationSettings.dueDateReminder,
                                uiState.notificationSettings.newBookReminder,
                            )
                            isNotificationSettingsVisible = true
                        }
                    }
                }
                BookOnMyScreenEvent.ReadingMarathonLinkRequested -> onReadingMarathonLinkClick()
                BookOnMyScreenEvent.LogoutClicked -> isLogoutDialogVisible = true
                BookOnMyScreenEvent.RetryClicked -> viewModel.refresh()
            }
        },
    )

    if (isLogoutDialogVisible) {
        BookOnLogoutAlertDialog(
            onDismissRequest = { isLogoutDialogVisible = false },
            onLogoutRequest = onLogoutRequest,
        )
    }

    if (isNotificationSettingsVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { isNotificationSettingsVisible = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = null,
        ) {
            BookOnNotificationSettingsBottomSheetContent(
                notificationSelections = notificationSelections,
                onCheckedChange = { index, checked ->
                    notificationSelections = notificationSelections.mapIndexed { selectionIndex, selected ->
                        if (selectionIndex == index) checked else selected
                    }
                },
                onCompleteClick = {
                    viewModel.updateNotifications(
                        dueDateReminder = notificationSelections.getOrElse(0) { false },
                        newBookReminder = notificationSelections.getOrElse(1) { false },
                    )
                    isNotificationSettingsVisible = false
                },
            )
        }
    }
}

/**
 * 로그아웃 확인을 앱 테마가 아닌 Android 기본 AlertDialog 외형으로 표시한다.
 * Compose 화면이 사라질 때 Dialog를 즉시 해제해 Activity 참조가 남지 않게 한다.
 */
@Composable
private fun BookOnLogoutAlertDialog(
    onDismissRequest: () -> Unit,
    onLogoutRequest: () -> Unit,
) {
    val context = LocalContext.current
    val logoutTitle = stringResource(R.string.action_logout)
    val logoutMessage = stringResource(R.string.logout_confirmation_message)
    val cancelLabel = stringResource(R.string.action_cancel)
    val currentOnDismissRequest by rememberUpdatedState(onDismissRequest)
    val currentOnLogoutRequest by rememberUpdatedState(onLogoutRequest)

    DisposableEffect(context, logoutTitle, logoutMessage, cancelLabel) {
        val logoutDialog = AlertDialog.Builder(context)
            .setTitle(logoutTitle)
            .setMessage(logoutMessage)
            .setPositiveButton(logoutTitle) { _, _ ->
                currentOnDismissRequest()
                currentOnLogoutRequest()
            }
            .setNegativeButton(cancelLabel, null)
            .create()
            .apply {
                setOnDismissListener { currentOnDismissRequest() }
                show()
            }

        onDispose {
            logoutDialog.setOnDismissListener(null)
            logoutDialog.dismiss()
        }
    }
}
