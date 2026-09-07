package com.teamnative.bookon.feature.fcm.domain

import com.teamnative.bookon.core.network.NetworkResult

interface FcmRepository {
    suspend fun registerToken(token: String, platform: String = "android"): NetworkResult<FcmTokenRegistration>
    suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregistration>
}

data class FcmTokenRegistration(val registered: Boolean)
data class FcmTokenUnregistration(val unregistered: Boolean)
