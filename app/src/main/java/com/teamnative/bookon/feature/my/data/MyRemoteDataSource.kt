package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface MyRemoteDataSource {
    suspend fun me(): NetworkResult<MyPageDto>
    suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean): NetworkResult<NotificationSettingsDto>
    suspend fun currentLoans(): NetworkResult<LoanListDto>
    suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<LoanListDto>
    suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPageDto>
}
class MyRemoteDataSourceImpl @Inject constructor(private val api: MyApiService, private val executor: ApiExecutor) : MyRemoteDataSource {
    override suspend fun me() = executor.execute { api.me() }
    override suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean) = executor.execute { api.updateNotificationSettings(NotificationSettingsRequestDto(dueDateReminder, newBookReminder)) }
    override suspend fun currentLoans() = executor.execute { api.currentLoans() }
    override suspend fun loanHistory(page: Int, size: Int, status: String) = executor.execute { api.loanHistory(page, size, status) }
    override suspend fun favorites(page: Int, size: Int) = executor.execute { api.favorites(page, size) }
}
