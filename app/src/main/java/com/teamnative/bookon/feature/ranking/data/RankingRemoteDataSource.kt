package com.teamnative.bookon.feature.ranking.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface RankingRemoteDataSource { suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRankingDto> }
class RankingRemoteDataSourceImpl @Inject constructor(private val api: RankingApiService, private val executor: ApiExecutor) : RankingRemoteDataSource {
    override suspend fun readers(year: Int, limit: Int) = executor.execute { api.readers(year, limit) }
}
