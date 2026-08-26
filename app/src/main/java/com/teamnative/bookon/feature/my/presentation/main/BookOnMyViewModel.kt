package com.teamnative.bookon.feature.my.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel
import com.teamnative.bookon.feature.my.domain.GetMyProfileUseCase
import com.teamnative.bookon.feature.my.domain.UploadProfileImageUseCase
import com.teamnative.bookon.feature.my.domain.UpdateNotificationSettingsUseCase
import com.teamnative.bookon.feature.marathon.domain.GetRead365MyInfoUseCase
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        initialMyUiState().copy(isInitialLoading = true),
    )
    val uiState: StateFlow<BookOnMyScreenUiState> = mutableUiState.asStateFlow()
    init { refresh(); refreshRead365Link() }
    /** 마이페이지 진입 시 사용자·대출 요약과 알림 설정을 서버에서 조회한다. */
    fun refresh() = viewModelScope.launch {
        mutableUiState.value = mutableUiState.value.copy(errorMessage = null)
        when (val result = getMyProfile()) {
            is NetworkResult.Success -> mutableUiState.value = mutableUiState.value.copy(
                userNameText = "${result.data.name} 님",
                studentInfoText = result.data.department,
                profileImageUrl = result.data.profileImageUrl,
                stats = listOf(
                    BookOnStatItemUiModel("대출 중", "${result.data.currentLoanCount}권"),
                    BookOnStatItemUiModel("반납 임박", "${result.data.overdueCount}권"),
                    BookOnStatItemUiModel("누적 대출", "${result.data.totalLoanCount}권"),
                ),
                notificationSettings = result.data.notificationSettings,
                profileImageErrorMessage = null,
                isInitialLoading = false,
            )
            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                isInitialLoading = false,
                errorMessage = result.error.toUiMessage(),
            )
        }
    }

    /** 사용자가 선택한 프로필 이미지를 업로드하고 성공한 URL을 Avatar 상태에 반영한다. */
    fun uploadProfileImage(contentType: String, imageBytes: ByteArray) = viewModelScope.launch {
        if (mutableUiState.value.isProfileImageUploading) {
            return@launch
        }

        mutableUiState.value = mutableUiState.value.copy(
            isProfileImageUploading = true,
            profileImageErrorMessage = null,
        )

        when (val result = uploadProfileImageUseCase(contentType, imageBytes)) {
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
    fun updateNotifications(dueDateReminder: Boolean, newBookReminder: Boolean) = viewModelScope.launch {
        when (val result = updateNotificationSettings(dueDateReminder, newBookReminder)) {
            is NetworkResult.Success -> {
                mutableUiState.value = mutableUiState.value.copy(notificationSettings = result.data)
            }
            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                errorMessage = result.error.toUiMessage(),
            )
        }
    }

    /** read365 세션 없음(4014)은 앱 로그아웃이 아닌 연동 필요 상태로 표시한다. */
    private fun refreshRead365Link() = viewModelScope.launch {
        when (val result = getRead365MyInfo()) {
            is NetworkResult.Success -> mutableUiState.value = mutableUiState.value.copy(isReadingMarathonLinked = true)
            is NetworkResult.Failure -> {
                val isLinkRequired = (result.error as? com.teamnative.bookon.core.network.NetworkError.Http)?.errorCode == 4014
                if (isLinkRequired) mutableUiState.value = mutableUiState.value.copy(isReadingMarathonLinked = false)
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
