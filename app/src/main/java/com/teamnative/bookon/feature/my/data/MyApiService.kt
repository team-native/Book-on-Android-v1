package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import okhttp3.RequestBody

interface MyApiService {
    @GET("me")
    suspend fun me(): Response<ApiEnvelope<MyPageDto>>

    @PATCH("me/notification-settings")
    suspend fun updateNotificationSettings(
        @Body body: NotificationSettingsRequestDto,
    ): Response<ApiEnvelope<NotificationSettingsDto>>

    @PATCH("me")
    suspend fun updateProfile(
        @Body body: UpdateMyProfileRequestDto,
    ): Response<ApiEnvelope<UpdateMyProfileResponseDto>>

    @POST("me/delete-request")
    suspend fun requestAccountDeletion(
        @Body body: AccountDeletionRequestDto,
    ): Response<ApiEnvelope<AccountDeletionResponseDto>>

    @POST("me/profile-image")
    suspend fun uploadProfileImage(
        @Body body: RequestBody,
    ): Response<ApiEnvelope<ProfileImageDto>>

    @DELETE("me/profile-image")
    suspend fun deleteProfileImage(): Response<ApiEnvelope<ProfileImageDto>>

    @GET("me/loans/current")
    suspend fun currentLoans(): Response<ApiEnvelope<LoanListDto>>

    @GET("me/loans/history")
    suspend fun loanHistory(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("status") status: String? = null,
    ): Response<ApiEnvelope<LoanListDto>>

    @GET("me/favorite-books")
    suspend fun favorites(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
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
    @SerialName("grade") val grade: Int? = null,
    @SerialName("classNo") val classNo: Int? = null,
    @SerialName("studentNo") val studentNo: String? = null,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null,
)

@Serializable
data class UpdateMyProfileRequestDto(
    @SerialName("name") val name: String? = null,
    @SerialName("department") val department: String? = null,
    @SerialName("grade") val grade: Int? = null,
    @SerialName("classNo") val classNo: Int? = null,
    @SerialName("studentNo") val studentNo: String? = null,
)

@Serializable
data class UpdateMyProfileResponseDto(
    @SerialName("user") val user: UserDto,
)

@Serializable
data class AccountDeletionRequestDto(
    @SerialName("reason") val reason: String? = null,
)

@Serializable
data class AccountDeletionResponseDto(
    @SerialName("requestId") val requestId: Long,
    @SerialName("status") val status: String,
    @SerialName("requestedAt") val requestedAt: String,
)

@Serializable
data class ProfileImageDto(
    @SerialName("profileImageUrl") val profileImageUrl: String? = null,
)

@Serializable
data class LoanSummaryDto(
    @SerialName("currentLoanCount") val currentLoanCount: Int,
    @SerialName("overdueCount") val overdueCount: Int,
    @SerialName("totalLoanCount") val totalLoanCount: Int = 0,
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
