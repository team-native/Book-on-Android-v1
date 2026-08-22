package com.teamnative.bookon.feature.community.domain

import javax.inject.Inject

class UpdateReviewUseCase @Inject constructor(
    private val repository: CommunityRepository,
) {
    /** 본인 리뷰의 평점 또는 본문을 수정한다. */
    suspend operator fun invoke(reviewId: Long, rating: Int? = null, content: String? = null) =
        repository.updateReview(reviewId, rating, content)
}

class DeleteReviewUseCase @Inject constructor(
    private val repository: CommunityRepository,
) {
    /** 본인 리뷰를 삭제한다. */
    suspend operator fun invoke(reviewId: Long) = repository.deleteReview(reviewId)
}

class CreateReviewCommentUseCase @Inject constructor(
    private val repository: CommunityRepository,
) {
    /** 리뷰에 댓글 또는 대댓글을 등록한다. */
    suspend operator fun invoke(reviewId: Long, content: String, parentCommentId: Long? = null) =
        repository.createComment(reviewId, content, parentCommentId)
}

class GetCommunityBookCategoriesUseCase @Inject constructor(
    private val repository: CommunityRepository,
) {
    /** 커뮤니티에서 사용할 KDC 책 분류 목록을 조회한다. */
    suspend operator fun invoke() = repository.bookCategories()
}
