package com.teamnative.bookon.feature.fcm.domain

import com.teamnative.bookon.core.network.NetworkResult

interface FcmRepository {
    suspend fun registerToken(
        token: String,
        platform: String = "android"
    ): NetworkResult<FcmTokenRegistration>
    suspend fun unregisterToken(
        token: String,
        cleanup: com.teamnative.bookon.feature.auth.domain.SessionCleanupHandle
    ): NetworkResult<FcmTokenUnregistration> =
    NetworkResult.Failure(com.teamnative.bookon.core.network.NetworkError.EmptyBody("Cleanup unavailable"))
    suspend fun registerToken(
        token: String,
        snapshot: com.teamnative.bookon.core.network.auth.SessionSnapshot
    ): NetworkResult<FcmTokenRegistration> = registerToken(token)
    suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregistration>
}

data class FcmTokenRegistration(val registered: Boolean)
data class FcmTokenUnregistration(val unregistered: Boolean)
