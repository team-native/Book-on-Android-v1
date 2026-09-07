package com.teamnative.bookon.feature.my.presentation.main

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.component.loading.BookOnLoadingScreen
import com.teamnative.bookon.core.ui.model.BookOnMenuRowUiModel
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel
import com.teamnative.bookon.feature.my.presentation.component.BookOnNotificationSettingsBottomSheetContent
import com.teamnative.bookon.feature.my.presentation.model.BookOnMyMarathonUiModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val PasswordChangeMenuIndex = 0
private const val LoanHistoryMenuIndex = 1
private const val FavoriteMenuIndex = 2
private const val NotificationSettingsMenuIndex = 3
private const val MAX_PROFILE_IMAGE_DIMENSION = 1_024
private const val PROFILE_IMAGE_JPEG_QUALITY = 85
private const val MAX_PROFILE_IMAGE_BYTES = 5 * 1024 * 1024
private val InitialNotificationSelections = listOf(false, false)

/** 서버에서 받은 내 서재 상태와 메뉴·로그아웃 이벤트를 화면에 연결한다. */
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

    val applicationContext = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { selectedUri ->
        if (selectedUri != null) {
            coroutineScope.launch {
                val uploadPayload = withContext(Dispatchers.IO) {
                    applicationContext.contentResolver.readProfileImageUpload(selectedUri)
                }

                if (uploadPayload == null) {
                    viewModel.showProfileImageSelectionError()
                } else {
                    viewModel.uploadProfileImage(
                        contentType = uploadPayload.contentType,
                        imageBytes = uploadPayload.imageBytes,
                    )
                }
            }
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {
        // 결과와 무관하게 별도 UI 상태를 추적하지 않는다. 거부 시 로컬 알림이 표시되지 않을 뿐이다.
    }

    val marathonUiState = if (uiState.isReadingMarathonLinked) {
        BookOnMyMarathonUiModel(
            title = stringResource(R.string.reading_marathon),
            statusText = stringResource(R.string.reading_marathon_linked_status),
            progressText = stringResource(R.string.reading_marathon_information_unavailable),
            remainingText = stringResource(R.string.reading_marathon_information_later),
            percentText = "",
            linked = true,
            progress = 0f,
        )
    } else {
        BookOnMyMarathonUiModel(
            title = stringResource(R.string.reading_marathon),
            statusText = "",
            progressText = stringResource(R.string.reading_marathon_not_linked),
            remainingText = stringResource(R.string.reading_marathon_link_toggle_description),
            percentText = "",
            linked = false,
            progress = 0f,
        )
    }
    val menuRows = listOf(
        BookOnMenuRowUiModel(stringResource(R.string.change_password)),
        BookOnMenuRowUiModel(stringResource(R.string.loan_return_history)),
        BookOnMenuRowUiModel(stringResource(R.string.favorite_books)),
        BookOnMenuRowUiModel(stringResource(R.string.notification_settings)),
        BookOnMenuRowUiModel(stringResource(R.string.usage_guide)),
    )
    val screenUiState = uiState.copy(
        userNameText = if (uiState.userNameText.isBlank()) {
            stringResource(R.string.my_profile_unavailable)
        } else {
            stringResource(R.string.user_name_suffix_spaced, uiState.userNameText)
        },
        studentInfoText = uiState.studentInfoText.ifBlank {
            stringResource(R.string.action_retry_description)
        },
        stats = uiState.stats.ifEmpty {
            listOf(
                BookOnStatItemUiModel(
                    label = stringResource(R.string.loaning),
                    value = stringResource(R.string.value_unavailable),
                ),
                BookOnStatItemUiModel(
                    label = stringResource(R.string.return_due_soon),
                    value = stringResource(R.string.value_unavailable),
                ),
                BookOnStatItemUiModel(
                    label = stringResource(R.string.total_loan),
                    value = stringResource(R.string.value_unavailable),
                ),
            )
        },
        marathon = marathonUiState,
        menus = menuRows,
    )

    BookOnMyScreen(
        uiState = screenUiState,
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
                            // 바텀시트 행 순서(반납 알림, 도서부 공지 알림)와 동일한 순서로 맞춘다.
                            notificationSelections = listOf(
                                uiState.notificationSettings.dueDateReminder,
                                uiState.notificationSettings.noticeReminder,
                            )
                            isNotificationSettingsVisible = true
                        }
                    }
                }
                BookOnMyScreenEvent.ProfileImageEditClicked -> {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
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
                    val isNotificationPermissionMissing = ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) != PackageManager.PERMISSION_GRANTED
                    if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isNotificationPermissionMissing) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
                onCompleteClick = {
                    viewModel.updateNotifications(
                        dueDateReminder = notificationSelections.getOrElse(0) { false },
                        // 신간 도서 알림은 이 바텀시트에 별도 토글이 없어 서버에서 마지막으로 받은 값을 그대로 유지한다.
                        newBookReminder = uiState.notificationSettings.newBookReminder,
                        noticeReminder = notificationSelections.getOrElse(1) { false },
                    )
                    isNotificationSettingsVisible = false
                },
            )
        }
    }
}

private data class ProfileImageUploadPayload(
    val contentType: String,
    val imageBytes: ByteArray,
)

/** 선택한 이미지를 제한된 크기의 JPEG 바이트로 변환해 업로드 메모리 사용량을 제한한다. */
private fun ContentResolver.readProfileImageUpload(uri: Uri): ProfileImageUploadPayload? {
    val selectedContentType = getType(uri) ?: return null
    if (!selectedContentType.startsWith("image/")) {
        return null
    }

    return try {
        val imageSource = ImageDecoder.createSource(this, uri)
        val decodedBitmap = ImageDecoder.decodeBitmap(imageSource) { decoder, imageInfo, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE

            val sourceWidth = imageInfo.size.width
            val sourceHeight = imageInfo.size.height
            val sourceMaxDimension = maxOf(sourceWidth, sourceHeight)

            if (sourceMaxDimension > MAX_PROFILE_IMAGE_DIMENSION) {
                val scale = MAX_PROFILE_IMAGE_DIMENSION.toFloat() / sourceMaxDimension
                decoder.setTargetSize(
                    (sourceWidth * scale).roundToInt(),
                    (sourceHeight * scale).roundToInt(),
                )
            }
        }

        try {
            ByteArrayOutputStream().use { outputStream ->
                if (!decodedBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        PROFILE_IMAGE_JPEG_QUALITY,
                        outputStream,
                    )
                ) {
                    return null
                }

                val imageBytes = outputStream.toByteArray()
                if (imageBytes.size > MAX_PROFILE_IMAGE_BYTES) {
                    return null
                }

                ProfileImageUploadPayload(
                    contentType = "image/jpeg",
                    imageBytes = imageBytes,
                )
            }
        } finally {
            decodedBitmap.recycle()
        }
    } catch (exception: IOException) {
        null
    } catch (exception: RuntimeException) {
        null
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
