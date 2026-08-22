package com.teamnative.bookon.feature.system.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface HealthRemoteDataSource {
    suspend fun health(): NetworkResult<HealthDto>
}

class HealthRemoteDataSourceImpl @Inject constructor(
    private val api: HealthApiService,
    private val executor: ApiExecutor,
) : HealthRemoteDataSource {
    /** 서버 상태 확인 API를 공통 네트워크 결과로 감싼다. */
    override suspend fun health() = executor.execute { api.health() }
}
