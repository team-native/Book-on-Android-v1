package com.teamnative.bookon.feature.community.domain

import com.teamnative.bookon.core.network.NetworkResult

interface CommunityRepository {
    suspend fun updateReview(reviewId: Long, rating: Int?, content: String?): NetworkResult<Review>
    suspend fun deleteReview(reviewId: Long): NetworkResult<Unit>
    suspend fun createComment(reviewId: Long, content: String, parentCommentId: Long?): NetworkResult<Comment>
    suspend fun bookCategories(): NetworkResult<List<BookCategory>>
}

data class Review(val reviewId: Long, val rating: Int, val content: String, val updatedAt: String)
data class Comment(
    val commentId: Long,
    val reviewId: Long,
    val parentCommentId: Long?,
    val content: String,
    val createdAt: String,
)
data class BookCategory(val code: String, val name: String, val depth: Int)
