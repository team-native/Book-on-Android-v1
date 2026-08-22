package com.teamnative.bookon.feature.my.domain

import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke() = repository.profile()
}

class UpdateNotificationSettingsUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(
        dueDateReminder: Boolean,
        newBookReminder: Boolean,
    ) = repository.updateNotificationSettings(dueDateReminder, newBookReminder)
}

class UpdateMyProfileUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    /** 사용자가 수정한 프로필 선택 필드를 서버에 반영한다. */
    suspend operator fun invoke(
        name: String? = null,
        department: String? = null,
        grade: Int? = null,
        classNo: Int? = null,
        studentNo: String? = null,
    ) = repository.updateProfile(name, department, grade, classNo, studentNo)
}

class RequestAccountDeletionUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    /** 계정 삭제 요청을 서버에 접수하고 요청 상태를 반환한다. */
    suspend operator fun invoke(reason: String? = null) = repository.requestAccountDeletion(reason)
}

class UploadProfileImageUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    /** 프로필 이미지 바이트를 서버가 요구하는 MIME 타입으로 업로드한다. */
    suspend operator fun invoke(contentType: String, imageBytes: ByteArray) = repository.uploadProfileImage(contentType, imageBytes)
}

class DeleteProfileImageUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    /** 서버에 저장된 프로필 이미지를 기본 이미지로 되돌린다. */
    suspend operator fun invoke() = repository.deleteProfileImage()
}

class GetCurrentLoansUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke() = repository.currentLoans()
}

class GetLoanHistoryUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(
        page: Int,
        size: Int,
        status: String,
    ) = repository.loanHistory(page, size, status)
}

class GetFavoriteBooksUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int) = repository.favorites(page, size)
}
