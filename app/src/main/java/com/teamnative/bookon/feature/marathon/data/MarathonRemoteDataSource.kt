package com.teamnative.bookon.feature.marathon.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface MarathonRemoteDataSource {
    suspend fun myInfo(): NetworkResult<Read365MyInfoDto>
}

class MarathonRemoteDataSourceImpl @Inject constructor(
    private val api: MarathonApiService,
    private val executor: ApiExecutor,
) : MarathonRemoteDataSource {
    override suspend fun myInfo() = executor.execute { api.myInfo() }
}
