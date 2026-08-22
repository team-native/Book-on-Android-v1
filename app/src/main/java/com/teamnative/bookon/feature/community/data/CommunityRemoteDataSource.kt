package com.teamnative.bookon.feature.community.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface CommunityRemoteDataSource {
    suspend fun updateReview(reviewId: Long, rating: Int?, content: String?): NetworkResult<ReviewDto>
    suspend fun deleteReview(reviewId: Long): NetworkResult<Unit>
    suspend fun createComment(reviewId: Long, content: String, parentCommentId: Long?): NetworkResult<CommentDto>
    suspend fun bookCategories(): NetworkResult<BookCategoryListDto>
}

class CommunityRemoteDataSourceImpl @Inject constructor(
    private val authenticatedApi: CommunityAuthenticatedApiService,
    private val publicApi: CommunityPublicApiService,
    private val executor: ApiExecutor,
) : CommunityRemoteDataSource {
    /** 본인 리뷰의 명시된 선택 필드를 수정한다. */
    override suspend fun updateReview(reviewId: Long, rating: Int?, content: String?) = executor.execute {
        authenticatedApi.updateReview(reviewId, ReviewUpdateRequestDto(rating, content))
    }

    /** 본인 리뷰를 삭제하고 null data 성공 응답을 Unit으로 변환한다. */
    override suspend fun deleteReview(reviewId: Long) = executor.executeUnit {
        authenticatedApi.deleteReview(reviewId)
    }

    /** 리뷰에 댓글 또는 대댓글을 작성한다. */
    override suspend fun createComment(reviewId: Long, content: String, parentCommentId: Long?) = executor.execute {
        authenticatedApi.createComment(reviewId, CommentCreateRequestDto(content, parentCommentId))
    }

    /** KDC 책 분류 목록을 공개 API에서 조회한다. */
    override suspend fun bookCategories() = executor.execute { publicApi.bookCategories() }
}
