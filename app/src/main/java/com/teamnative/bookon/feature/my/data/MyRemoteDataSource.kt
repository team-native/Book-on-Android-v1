package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

interface MyRemoteDataSource {
    suspend fun me(): NetworkResult<MyPageDto>
    suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean, noticeReminder: Boolean): NetworkResult<NotificationSettingsDto>
    suspend fun updateProfile(request: UpdateMyProfileRequestDto): NetworkResult<UpdateMyProfileResponseDto>
    suspend fun requestAccountDeletion(reason: String?): NetworkResult<AccountDeletionResponseDto>
    suspend fun uploadProfileImage(contentType: String, imageBytes: ByteArray): NetworkResult<ProfileImageDto>
    suspend fun deleteProfileImage(): NetworkResult<ProfileImageDto>
    suspend fun currentLoans(): NetworkResult<LoanListDto>
    suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<LoanListDto>
    suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPageDto>
}
class MyRemoteDataSourceImpl @Inject constructor(private val api: MyApiService, private val executor: ApiExecutor) : MyRemoteDataSource {
    override suspend fun me() = executor.execute { api.me() }
    override suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean, noticeReminder: Boolean) = executor.execute { api.updateNotificationSettings(NotificationSettingsRequestDto(dueDateReminder, newBookReminder, noticeReminder)) }
    override suspend fun updateProfile(request: UpdateMyProfileRequestDto) = executor.execute { api.updateProfile(request) }
    override suspend fun requestAccountDeletion(reason: String?) = executor.execute { api.requestAccountDeletion(AccountDeletionRequestDto(reason)) }
    override suspend fun uploadProfileImage(contentType: String, imageBytes: ByteArray) = executor.execute {
        api.uploadProfileImage(imageBytes.toRequestBody(contentType.toMediaType()))
    }
    override suspend fun deleteProfileImage() = executor.execute { api.deleteProfileImage() }
    override suspend fun currentLoans() = executor.execute { api.currentLoans() }
    override suspend fun loanHistory(page: Int, size: Int, status: String) = executor.execute { api.loanHistory(page, size, status) }
    override suspend fun favorites(page: Int, size: Int) = executor.execute { api.favorites(page, size) }
}
