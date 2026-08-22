package com.teamnative.bookon.feature.system.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.system.domain.HealthRepository
import com.teamnative.bookon.feature.system.domain.HealthStatus
import javax.inject.Inject

class HealthRepositoryImpl @Inject constructor(
    private val remote: HealthRemoteDataSource,
) : HealthRepository {
    override suspend fun health(): NetworkResult<HealthStatus> = remote.health().map { HealthStatus(it.status) }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
