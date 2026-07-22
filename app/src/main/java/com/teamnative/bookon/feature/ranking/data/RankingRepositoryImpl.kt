package com.teamnative.bookon.feature.ranking.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.ranking.domain.Reader
import com.teamnative.bookon.feature.ranking.domain.ReaderRanking
import com.teamnative.bookon.feature.ranking.domain.RankingRepository
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(private val remote: RankingRemoteDataSource) : RankingRepository {
    override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> = when (val result = remote.readers(year, limit)) {
        is NetworkResult.Success -> NetworkResult.Success(ReaderRanking(result.data.year, result.data.resetPolicy, result.data.items.map { Reader(it.rank, it.name, it.department, it.loanCount) }))
        is NetworkResult.Failure -> result
    }
}
