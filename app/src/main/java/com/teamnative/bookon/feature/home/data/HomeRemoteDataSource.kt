package com.teamnative.bookon.feature.home.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface HomeRemoteDataSource {
    suspend fun home(limit: Int): NetworkResult<HomeDto>
    suspend fun notices(page: Int, size: Int): NetworkResult<NoticePageDto>
}

class HomeRemoteDataSourceImpl @Inject constructor(
    private val api: HomeApiService,
    private val executor: ApiExecutor,
) : HomeRemoteDataSource {
    override suspend fun home(limit: Int) = executor.execute { api.home(limit) }
    override suspend fun notices(page: Int, size: Int) = executor.execute { api.notices(page, size) }
}
