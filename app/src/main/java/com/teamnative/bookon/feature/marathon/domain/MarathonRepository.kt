package com.teamnative.bookon.feature.marathon.domain

import com.teamnative.bookon.core.network.NetworkResult

interface MarathonRepository {
    suspend fun read365MyInfo(): NetworkResult<Read365MyInfo>
}

data class Read365MyInfo(val read365Id: String)
