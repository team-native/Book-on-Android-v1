package com.teamnative.bookon.feature.community.data

import com.teamnative.bookon.core.network.ApiEnvelope
import com.teamnative.bookon.core.network.EmptyResponseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/** 명세에서 요청·응답 필드가 확정된 리뷰·댓글 변경 HTTP 계약이다. */
interface CommunityAuthenticatedApiService {
    @PATCH("reviews/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: Long,
        @Body body: ReviewUpdateRequestDto,
    ): Response<ApiEnvelope<ReviewDto>>

    @DELETE("reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Long,
    ): Response<ApiEnvelope<EmptyResponseDto>>

    @POST("reviews/{reviewId}/comments")
    suspend fun createComment(
        @Path("reviewId") reviewId: Long,
        @Body body: CommentCreateRequestDto,
    ): Response<ApiEnvelope<CommentDto>>
}

interface CommunityPublicApiService {
    @GET("community/book-categories")
    suspend fun bookCategories(): Response<ApiEnvelope<BookCategoryListDto>>
}

@Serializable
data class ReviewUpdateRequestDto(
    @SerialName("rating") val rating: Int? = null,
    @SerialName("content") val content: String? = null,
)

@Serializable
data class ReviewDto(
    @SerialName("reviewId") val reviewId: Long,
    @SerialName("rating") val rating: Int,
    @SerialName("content") val content: String,
    @SerialName("updatedAt") val updatedAt: String,
)

@Serializable
data class CommentCreateRequestDto(
    @SerialName("content") val content: String,
    @SerialName("parentCommentId") val parentCommentId: Long? = null,
)

@Serializable
data class CommentDto(
    @SerialName("commentId") val commentId: Long,
    @SerialName("reviewId") val reviewId: Long,
    @SerialName("parentCommentId") val parentCommentId: Long? = null,
    @SerialName("content") val content: String,
    @SerialName("createdAt") val createdAt: String,
)

@Serializable
data class BookCategoryListDto(
    @SerialName("items") val items: List<BookCategoryDto>,
)

@Serializable
data class BookCategoryDto(
    @SerialName("code") val code: String,
    @SerialName("name") val name: String,
    @SerialName("depth") val depth: Int,
)
