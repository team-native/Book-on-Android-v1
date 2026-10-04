package com.teamnative.bookon.feature.my.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel
import com.teamnative.bookon.feature.my.domain.GetMyProfileUseCase
import com.teamnative.bookon.feature.my.domain.RequestAccountDeletionUseCase
import com.teamnative.bookon.feature.my.domain.UploadProfileImageUseCase
import com.teamnative.bookon.feature.my.domain.UpdateNotificationSettingsUseCase
import com.teamnative.bookon.feature.fcm.domain.ClearFcmTokenOnLogoutUseCase
import com.teamnative.bookon.feature.marathon.domain.GetRead365MyInfoUseCase
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookOnMyViewModel @Inject constructor(
    private val getMyProfile: GetMyProfileUseCase,
    private val updateNotificationSettings: UpdateNotificationSettingsUseCase,
    private val getRead365MyInfo: GetRead365MyInfoUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val requestAccountDeletionUseCase: RequestAccountDeletionUseCase,
    private val clearFcmTokenOnLogoutUseCase: ClearFcmTokenOnLogoutUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        initialMyUiState(),
    )
    val uiState: StateFlow<BookOnMyScreenUiState> = mutableUiState.asStateFlow()
    private var profileRefreshJob: Job? = null
    private var read365RefreshJob: Job? = null
    private var notificationRevision = 0L
    private var read365Generation = 0L
    private var hasResumed = false
    private var profileImageRevision = 0L

    init {
        refreshProfile()
        refreshRead365Link()
    }

    /** 마이페이지 진입과 재시도에서 프로필과 Read365 연동 상태를 함께 다시 조회한다. */
    fun refresh() {
        refreshProfile()
        refreshRead365Link(force = true)
    }

    /** 최초 구성을 제외한 화면 복귀에서는 외부에서 바뀔 수 있는 연동 상태를 재조회한다. */
    fun onResumed() {
        if (!hasResumed) {
            hasResumed = true
            return
        }
        refreshRead365Link(force = true)
    }

    /** 사용자·대출 요약과 알림 설정을 조회하며 진행 중인 중복 요청은 만들지 않는다. */
    private fun refreshProfile() {
        if (profileRefreshJob?.isActive == true) {
            return
        }

        val capturedNotificationRevision = notificationRevision
        val capturedProfileImageRevision = profileImageRevision
        profileRefreshJob = viewModelScope.launch {
            mutableUiState.value = mutableUiState.value.copy(errorMessage = null)
            when (val result = getMyProfile()) {
                is NetworkResult.Success -> mutableUiState.value = mutableUiState.value.copy(
                    userNameText = result.data.name,
                    studentInfoText = result.data.department,
                    profileImageUrl = if (capturedProfileImageRevision == profileImageRevision &&
                        !mutableUiState.value.isProfileImageUploading
                    ) {
                        result.data.profileImageUrl
                    } else {
                        mutableUiState.value.profileImageUrl
                    },
                    stats = listOf(
                        BookOnStatItemUiModel("대출 중", "${result.data.currentLoanCount}권"),
                        BookOnStatItemUiModel("반납 임박", "${result.data.overdueCount}권"),
                        BookOnStatItemUiModel("누적 대출", "${result.data.totalLoanCount}권"),
                    ),
                    notificationSettings = if (capturedNotificationRevision == notificationRevision &&
                        !mutableUiState.value.isNotificationSaving
                    ) {
                        result.data.notificationSettings
                    } else {
                        mutableUiState.value.notificationSettings
                    },
                    profileImageErrorMessage = if (capturedProfileImageRevision == profileImageRevision &&
                        !mutableUiState.value.isProfileImageUploading
                    ) {
                        null
                    } else {
                        mutableUiState.value.profileImageErrorMessage
                    },
                    isInitialLoading = false,
                )
                is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                    isInitialLoading = false,
                    errorMessage = result.error.toUiMessage(),
                )
            }
        }
    }

    /** 사용자가 선택한 프로필 이미지를 업로드하고 성공한 URL을 Avatar 상태에 반영한다. */
    fun uploadProfileImage(contentType: String, imageBytes: ByteArray) = viewModelScope.launch {
        if (mutableUiState.value.isProfileImageUploading) {
            return@launch
        }

        profileImageRevision += 1L
        mutableUiState.value = mutableUiState.value.copy(
            isProfileImageUploading = true,
            profileImageErrorMessage = null,
        )

        val result = uploadProfileImageUseCase(contentType, imageBytes)
        profileImageRevision += 1L
        when (result) {
            is NetworkResult.Success -> mutableUiState.value = mutableUiState.value.copy(
                profileImageUrl = result.data.profileImageUrl,
                isProfileImageUploading = false,
            )
            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                isProfileImageUploading = false,
                profileImageErrorMessage = result.error.toProfileImageUiMessage(),
            )
        }
    }

    /** 이미지 선택 또는 변환에 실패했을 때 프로필 영역에 표시할 오류 상태를 설정한다. */
    fun showProfileImageSelectionError() {
        mutableUiState.value = mutableUiState.value.copy(
            profileImageErrorMessage = BookOnUiMessage.Resource(R.string.error_upload_profile_image),
        )
    }

    /** 알림 설정 완료 시 서버 값을 갱신하고 성공하면 마이페이지 정보를 다시 불러온다. */
    fun updateNotifications(
        dueDateReminder: Boolean,
        newBookReminder: Boolean,
        noticeReminder: Boolean,
        onSuccess: () -> Unit = {},
    ) {
        if (mutableUiState.value.isNotificationSaving) {
            return
        }
        notificationRevision += 1L
        mutableUiState.value = mutableUiState.value.copy(
            isNotificationSaving = true,
            notificationSaveError = null,
        )
        viewModelScope.launch {
            val notificationResult = updateNotificationSettings(dueDateReminder, newBookReminder, noticeReminder)
            notificationRevision += 1L
            when (notificationResult) {
                is NetworkResult.Success -> {
                    mutableUiState.value = mutableUiState.value.copy(
                        notificationSettings = notificationResult.data,
                        isNotificationSaving = false,
                    )
                    onSuccess()
                }
                is NetworkResult.Failure -> {
                    mutableUiState.value = mutableUiState.value.copy(
                        isNotificationSaving = false,
                        notificationSaveError = when (val error = notificationResult.error) {
                            is NetworkError.Http -> BookOnUiMessage.Dynamic(error.message)
                            else -> BookOnUiMessage.Resource(R.string.error_save_notifications)
                        },
                    )
                }
            }
        }
    }

    /**
     * 회원 탈퇴를 서버에 요청하고, 접수에 성공하면 이 기기의 FCM 토큰을 해제한 뒤 onSuccess로 세션 종료를 위임한다.
     * onSuccess는 호출부(향후 UI)가 BookOnSessionViewModel.logout()과 동일한 세션 종료 콜백을 넘겨야 한다.
     * 서버 delete-request 엔드포인트가 아직 없어(백엔드 미구현) 현재는 항상 실패로 응답한다.
     */
    fun requestAccountDeletion(reason: String? = null, onSuccess: () -> Unit) = viewModelScope.launch {
        if (mutableUiState.value.isAccountDeletionInProgress) {
            return@launch
        }

        mutableUiState.value = mutableUiState.value.copy(
            isAccountDeletionInProgress = true,
            accountDeletionErrorMessage = null,
        )

        when (val result = requestAccountDeletionUseCase(reason)) {
            is NetworkResult.Success -> {
                clearFcmTokenOnLogoutUseCase()
                mutableUiState.value = mutableUiState.value.copy(isAccountDeletionInProgress = false)
                onSuccess()
            }
            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                isAccountDeletionInProgress = false,
                accountDeletionErrorMessage = result.error.toUiMessage(),
            )
        }
    }

    /** read365 세션 없음(4014)은 앱 로그아웃이 아닌 연동 필요 상태로 표시한다. */
    private fun refreshRead365Link(force: Boolean = false) {
        if (!force && read365RefreshJob?.isActive == true) {
            return
        }

        read365RefreshJob?.cancel()
        read365Generation += 1L
        val capturedGeneration = read365Generation
        read365RefreshJob = viewModelScope.launch {
            mutableUiState.value = mutableUiState.value.copy(read365ErrorMessage = null)
            val result = getRead365MyInfo()
            if (capturedGeneration != read365Generation) {
                return@launch
            }
            when (result) {
                is NetworkResult.Success -> mutableUiState.value = mutableUiState.value.copy(
                    isReadingMarathonLinked = true,
                )
                is NetworkResult.Failure -> {
                    val isLinkRequired = (result.error as? NetworkError.Http)?.errorCode == 4014
                    if (isLinkRequired) {
                        mutableUiState.value = mutableUiState.value.copy(
                            isReadingMarathonLinked = false,
                        )
                    } else {
                        mutableUiState.value = mutableUiState.value.copy(
                            read365ErrorMessage = BookOnUiMessage.Resource(R.string.error_load_read365),
                        )
                    }
                }
            }
        }
    }
}

private fun NetworkError.toUiMessage(): BookOnUiMessage = when (this) {
    is NetworkError.Http -> BookOnUiMessage.Dynamic(message)
    else -> BookOnUiMessage.Resource(R.string.error_load_my_page)
}

private fun NetworkError.toProfileImageUiMessage(): BookOnUiMessage = when (this) {
    is NetworkError.Http -> BookOnUiMessage.Dynamic(message)
    else -> BookOnUiMessage.Resource(R.string.error_upload_profile_image)
}
