package com.teamnative.bookon.feature.my.domain

import com.teamnative.bookon.core.network.NetworkResult

interface MyRepository {
    suspend fun profile(): NetworkResult<MyProfile>
    suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean): NetworkResult<NotificationSettings>
    suspend fun currentLoans(): NetworkResult<List<MyLoan>>
    suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<MyLoanPage>
    suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPage>
}
data class MyProfile(val name: String, val department: String, val currentLoanCount: Int, val overdueCount: Int, val notificationSettings: NotificationSettings)
data class NotificationSettings(val dueDateReminder: Boolean, val newBookReminder: Boolean)
data class MyLoan(
    val loanId: Long,
    val bookId: Long,
    val title: String,
    val dueDate: String?,
    val dDay: Int?,
    val status: String,
)
data class MyLoanPage(val items: List<MyLoan>, val page: Int, val hasNext: Boolean)
data class FavoriteBook(val bookId: Long, val title: String, val author: String, val libraryNumber: String, val loanAvailable: Boolean)
data class FavoriteBookPage(val items: List<FavoriteBook>, val page: Int, val hasNext: Boolean)
