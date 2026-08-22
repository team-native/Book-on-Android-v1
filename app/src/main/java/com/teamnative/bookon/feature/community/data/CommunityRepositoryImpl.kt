package com.teamnative.bookon.feature.community.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.community.domain.BookCategory
import com.teamnative.bookon.feature.community.domain.Comment
import com.teamnative.bookon.feature.community.domain.CommunityRepository
import com.teamnative.bookon.feature.community.domain.Review
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val remote: CommunityRemoteDataSource,
) : CommunityRepository {
    override suspend fun updateReview(reviewId: Long, rating: Int?, content: String?): NetworkResult<Review> =
        remote.updateReview(reviewId, rating, content).map { Review(it.reviewId, it.rating, it.content, it.updatedAt) }

    override suspend fun deleteReview(reviewId: Long): NetworkResult<Unit> = remote.deleteReview(reviewId)

    override suspend fun createComment(
        reviewId: Long,
        content: String,
        parentCommentId: Long?,
    ): NetworkResult<Comment> = remote.createComment(reviewId, content, parentCommentId).map {
        Comment(it.commentId, it.reviewId, it.parentCommentId, it.content, it.createdAt)
    }

    override suspend fun bookCategories(): NetworkResult<List<BookCategory>> = remote.bookCategories().map {
        it.items.map { category -> BookCategory(category.code, category.name, category.depth) }
    }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
