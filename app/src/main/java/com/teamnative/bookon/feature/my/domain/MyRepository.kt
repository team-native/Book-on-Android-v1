package com.teamnative.bookon.feature.my.domain

import com.teamnative.bookon.core.network.NetworkResult

interface MyRepository {
    suspend fun profile(): NetworkResult<MyProfile>
    suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean, noticeReminder: Boolean): NetworkResult<NotificationSettings>
    suspend fun updateProfile(
        name: String?,
        department: String?,
        grade: Int?,
        classNo: Int?,
        studentNo: String?,
    ): NetworkResult<MyUser>
    suspend fun requestAccountDeletion(reason: String?): NetworkResult<AccountDeletion>
    suspend fun uploadProfileImage(contentType: String, imageBytes: ByteArray): NetworkResult<ProfileImage>
    suspend fun deleteProfileImage(): NetworkResult<ProfileImage>
    suspend fun currentLoans(): NetworkResult<List<MyLoan>>
    suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<MyLoanPage>
    suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPage>
}
data class MyProfile(
    val name: String,
    val department: String,
    val profileImageUrl: String?,
    val currentLoanCount: Int,
    val overdueCount: Int,
    val totalLoanCount: Int?,
    val notificationSettings: NotificationSettings,
    val currentLoans: List<MyCurrentLoanSummary> = emptyList(),
) {
    val dueSoonCount: Int?
        get() = if (currentLoans.size != currentLoanCount || currentLoans.any { it.dDay == null }) {
            null
        } else {
            currentLoans.count { it.dDay in 0..3 }
        }
}

data class MyCurrentLoanSummary(val dDay: Int?, val dueDate: String?)
data class MyUser(
    val userId: Long,
    val email: String,
    val name: String,
    val department: String,
    val gender: String,
    val grade: Int?,
    val classNo: Int?,
    val studentNo: String?,
    val profileImageUrl: String?,
)
data class AccountDeletion(val requestId: Long, val status: String, val requestedAt: String)
data class ProfileImage(val profileImageUrl: String?)
data class NotificationSettings(val dueDateReminder: Boolean, val newBookReminder: Boolean, val noticeReminder: Boolean)
data class MyLoan(
    val loanId: Long,
    val bookId: Long,
    val title: String,
    val dueDate: String?,
    val dDay: Int?,
    val status: String,
    val extensionAvailable: Boolean = false,
)
data class MyLoanPage(val items: List<MyLoan>, val page: Int, val hasNext: Boolean)
data class FavoriteBook(val bookId: Long, val title: String, val author: String, val libraryNumber: String, val loanAvailable: Boolean)
data class FavoriteBookPage(val items: List<FavoriteBook>, val page: Int, val hasNext: Boolean)
