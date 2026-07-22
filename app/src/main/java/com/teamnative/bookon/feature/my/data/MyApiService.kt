package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface MyApiService {
    @GET("me")
    suspend fun me(): Response<ApiEnvelope<MyPageDto>>

    @PATCH("me/notification-settings")
    suspend fun updateNotificationSettings(
        @Body body: NotificationSettingsRequestDto,
    ): Response<ApiEnvelope<NotificationSettingsDto>>

    @GET("me/loans/current")
    suspend fun currentLoans(): Response<ApiEnvelope<LoanListDto>>

    @GET("me/loans/history")
    suspend fun loanHistory(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") status: String,
    ): Response<ApiEnvelope<LoanListDto>>

    @GET("me/favorite-books")
    suspend fun favorites(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiEnvelope<FavoriteBookPageDto>>
}
@Serializable
data class MyPageDto(
    @SerialName("user") val user: UserDto,
    @SerialName("loanSummary") val loanSummary: LoanSummaryDto,
    @SerialName("currentLoans") val currentLoans: List<UserLoanDto>,
    @SerialName("notificationSettings") val notificationSettings: NotificationSettingsDto,
)

@Serializable
data class UserDto(
    @SerialName("userId") val userId: Long,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("department") val department: String,
    @SerialName("gender") val gender: String,
)

@Serializable
data class LoanSummaryDto(
    @SerialName("currentLoanCount") val currentLoanCount: Int,
    @SerialName("overdueCount") val overdueCount: Int,
    @SerialName("nearestDueDate") val nearestDueDate: String? = null,
    @SerialName("nearestDueDday") val nearestDueDday: Int? = null,
)

@Serializable
data class UserLoanDto(
    @SerialName("loanId") val loanId: Long,
    @SerialName("bookId") val bookId: Long,
    @SerialName("title") val title: String,
    @SerialName("dueDate") val dueDate: String,
    @SerialName("dDay") val dDay: Int,
    @SerialName("extensionAvailable") val extensionAvailable: Boolean,
)

@Serializable
data class NotificationSettingsDto(
    @SerialName("dueDateReminder") val dueDateReminder: Boolean,
    @SerialName("newBookReminder") val newBookReminder: Boolean,
)

@Serializable
data class NotificationSettingsRequestDto(
    @SerialName("dueDateReminder") val dueDateReminder: Boolean,
    @SerialName("newBookReminder") val newBookReminder: Boolean,
)

@Serializable
data class LoanListDto(
    @SerialName("items") val items: List<LoanHistoryDto>,
    @SerialName("pagination") val pagination: PaginationDto? = null,
)

@Serializable
data class LoanHistoryDto(
    @SerialName("loanId") val loanId: Long,
    @SerialName("bookId") val bookId: Long,
    @SerialName("title") val title: String,
    @SerialName("borrowedAt") val borrowedAt: String? = null,
    @SerialName("dueDate") val dueDate: String? = null,
    @SerialName("returnedAt") val returnedAt: String? = null,
    @SerialName("dDay") val dDay: Int? = null,
    @SerialName("status") val status: String,
)

@Serializable
data class FavoriteBookPageDto(
    @SerialName("items") val items: List<FavoriteBookDto>,
    @SerialName("pagination") val pagination: PaginationDto? = null,
)

@Serializable
data class FavoriteBookDto(
    @SerialName("bookId") val bookId: Long,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("libraryNumber") val libraryNumber: String,
    @SerialName("availableQuantity") val availableQuantity: Int,
    @SerialName("loanAvailable") val loanAvailable: Boolean,
    @SerialName("favoritedAt") val favoritedAt: String,
)

@Serializable
data class PaginationDto(
    @SerialName("page") val page: Int,
    @SerialName("hasNext") val hasNext: Boolean,
)
