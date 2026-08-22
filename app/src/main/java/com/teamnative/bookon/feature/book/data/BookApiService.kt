package com.teamnative.bookon.feature.book.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** 로그인 여부와 무관하게 호출하는 도서 조회 HTTP 계약이다. */
interface BookPublicApiService {
    @GET("books")
    suspend fun books(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("category") category: String?,
    ): Response<ApiEnvelope<BookPageDto>>

    @GET("books/search")
    suspend fun search(
        @Query("keyword") keyword: String?,
        @Query("libraryNumber") libraryNumber: String?,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): Response<ApiEnvelope<BookPageDto>>

    @GET("books/new")
    suspend fun newBooks(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): Response<ApiEnvelope<BookPageDto>>

    @GET("books/categories")
    suspend fun categories(): Response<ApiEnvelope<CategoryListDto>>

    @GET("books/recommendations/today")
    suspend fun todayRecommendations(): Response<ApiEnvelope<TodayRecommendationsDto>>

    @GET("books/{bookId}/purchase-links")
    suspend fun purchaseLinks(
        @Path("bookId") bookId: Long,
    ): Response<ApiEnvelope<PurchaseLinkListDto>>
}

/** access token이 있으면 첨부하고, 없으면 선택 인증 상세 조회만 허용하는 도서 HTTP 계약이다. */
interface BookAuthenticatedApiService {
    @GET("books/{bookId}")
    suspend fun book(
        @Path("bookId") bookId: Long,
    ): Response<ApiEnvelope<BookDetailDto>>

    @POST("books/{bookId}/favorite")
    suspend fun addFavorite(
        @Path("bookId") bookId: Long,
    ): Response<ApiEnvelope<FavoriteDto>>

    @DELETE("books/{bookId}/favorite")
    suspend fun removeFavorite(
        @Path("bookId") bookId: Long,
    ): Response<ApiEnvelope<FavoriteDto>>

    @POST("loans")
    suspend fun loan(
        @Body body: LoanRequestDto,
    ): Response<ApiEnvelope<LoanDto>>

    @POST("loans/{loanId}/extension")
    suspend fun extendLoan(
        @Path("loanId") loanId: Long,
    ): Response<ApiEnvelope<LoanExtensionDto>>
}

@Serializable
data class BookPageDto(
    @SerialName("items") val items: List<BookDto>,
    @SerialName("pagination") val pagination: PaginationDto,
)

@Serializable
data class PaginationDto(
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("hasNext") val hasNext: Boolean,
)

@Serializable
data class BookDto(
    @SerialName("bookId") val bookId: Long,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("category") val category: String,
    @SerialName("libraryNumber") val libraryNumber: String,
    @SerialName("coverImageUrl") val coverImageUrl: String? = null,
    @SerialName("coverUrl") val coverUrl: String? = null,
    @SerialName("loanAvailable") val loanAvailable: Boolean,
    @SerialName("status") val status: String,
)

@Serializable
data class BookDetailDto(
    @SerialName("bookId") val bookId: Long,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("category") val category: String,
    @SerialName("libraryNumber") val libraryNumber: String,
    @SerialName("coverImageUrl") val coverImageUrl: String? = null,
    @SerialName("coverUrl") val coverUrl: String? = null,
    @SerialName("loanAvailable") val loanAvailable: Boolean,
    @SerialName("status") val status: String,
    @SerialName("description") val description: String? = null,
    @SerialName("favorite") val favorite: Boolean = false,
    @SerialName("locationName") val locationName: String? = null,
    @SerialName("returnPlanDate") val returnPlanDate: String? = null,
)

@Serializable
data class CategoryListDto(
    @SerialName("items") val items: List<CategoryDto>,
)

@Serializable
data class CategoryDto(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("code") val code: String,
    @SerialName("name") val name: String,
    @SerialName("bookCount") val bookCount: Int,
)

@Serializable
data class LoanRequestDto(
    @SerialName("bookId") val bookId: Long,
)

@Serializable
data class LoanDto(
    @SerialName("loanId") val loanId: Long,
    @SerialName("bookId") val bookId: Long,
    @SerialName("dueDate") val dueDate: String,
    @SerialName("status") val status: String,
    @SerialName("title") val title: String,
    @SerialName("extensionAvailable") val extensionAvailable: Boolean = false,
)

@Serializable
data class LoanExtensionDto(
    @SerialName("loanId") val loanId: Long,
    @SerialName("previousDueDate") val previousDueDate: String,
    @SerialName("newDueDate") val newDueDate: String,
    @SerialName("extensionCount") val extensionCount: Int,
    @SerialName("extensionAvailable") val extensionAvailable: Boolean,
)

@Serializable
data class TodayRecommendationsDto(
    @SerialName("recommendedAt") val recommendedAt: String,
    @SerialName("items") val items: List<TodayRecommendationItemDto>,
)

@Serializable
data class TodayRecommendationItemDto(
    @SerialName("bookId") val bookId: Long,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("coverImageUrl") val coverImageUrl: String? = null,
    @SerialName("coverUrl") val coverUrl: String? = null,
    @SerialName("reason") val reason: String? = null,
)

@Serializable
data class PurchaseLinkListDto(
    @SerialName("items") val items: List<PurchaseLinkDto>,
)

@Serializable
data class PurchaseLinkDto(
    @SerialName("provider") val provider: String,
    @SerialName("label") val label: String,
    @SerialName("url") val url: String,
)

@Serializable
data class FavoriteDto(
    @SerialName("bookId") val bookId: Long,
    @SerialName("favorite") val favorite: Boolean,
)
