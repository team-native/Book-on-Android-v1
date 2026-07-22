package com.teamnative.bookon.feature.home.domain

import com.teamnative.bookon.core.network.NetworkResult

interface HomeRepository {
    suspend fun home(limit: Int): NetworkResult<HomeData>
    suspend fun notices(page: Int, size: Int): NetworkResult<List<HomeNotice>>
}

data class HomeData(val todayRecommendation: HomeRecommendation?)
data class HomeRecommendation(val bookId: Long, val title: String, val author: String, val coverImageUrl: String?, val reason: String?)
data class HomeNotice(val noticeId: Long, val title: String, val summary: String, val createdAt: String)
