package com.teamnative.bookon.feature.home.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.home.domain.HomeData
import com.teamnative.bookon.feature.home.domain.HomeNotice
import com.teamnative.bookon.feature.home.domain.HomeRecommendation
import com.teamnative.bookon.feature.home.domain.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remote: HomeRemoteDataSource,
) : HomeRepository {
    override suspend fun home(limit: Int): NetworkResult<HomeData> = remote.home(limit).map { dto ->
        HomeData(dto.todayRecommendation?.let { recommendation ->
            HomeRecommendation(
                bookId = recommendation.bookId,
                title = recommendation.title,
                author = recommendation.author,
                coverImageUrl = recommendation.coverImageUrl,
                reason = recommendation.reason,
            )
        })
    }

    override suspend fun notices(page: Int, size: Int): NetworkResult<List<HomeNotice>> = remote.notices(page, size).map { pageDto ->
        pageDto.items.map { notice -> HomeNotice(notice.noticeId, notice.title, notice.summary, notice.createdAt) }
    }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
