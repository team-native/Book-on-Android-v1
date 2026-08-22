package com.teamnative.bookon.feature.marathon.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.marathon.domain.MarathonRepository
import com.teamnative.bookon.feature.marathon.domain.Read365MyInfo
import javax.inject.Inject

class MarathonRepositoryImpl @Inject constructor(
    private val remote: MarathonRemoteDataSource,
) : MarathonRepository {
    override suspend fun read365MyInfo(): NetworkResult<Read365MyInfo> = when (val result = remote.myInfo()) {
        is NetworkResult.Success -> NetworkResult.Success(
            Read365MyInfo(result.data.read365Id, result.data.memberKey, result.data.schoolKey),
        )
        is NetworkResult.Failure -> result
    }
}
