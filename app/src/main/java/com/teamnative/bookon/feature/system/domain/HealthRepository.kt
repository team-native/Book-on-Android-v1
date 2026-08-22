package com.teamnative.bookon.feature.system.domain

import com.teamnative.bookon.core.network.NetworkResult

interface HealthRepository {
    suspend fun health(): NetworkResult<HealthStatus>
}

data class HealthStatus(val status: String)
