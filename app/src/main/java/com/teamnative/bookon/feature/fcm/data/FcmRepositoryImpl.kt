package com.teamnative.bookon.feature.fcm.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.fcm.domain.FcmRepository
import com.teamnative.bookon.feature.fcm.domain.FcmTokenRegistration
import com.teamnative.bookon.feature.fcm.domain.FcmTokenUnregistration
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val remote: FcmRemoteDataSource,
) : FcmRepository {
    override suspend fun registerToken(
        token: String,
        platform: String
    ): NetworkResult<FcmTokenRegistration> =
    remote.registerToken(FcmTokenRegisterRequestDto(
            token = token,
            platform = platform
    ))
    .map {
        FcmTokenRegistration(it.registered)
    }

    override suspend fun registerToken(
        token: String,
        snapshot: com.teamnative.bookon.core.network.auth.SessionSnapshot
    ): NetworkResult<FcmTokenRegistration> =
    remote.registerToken(
        FcmTokenRegisterRequestDto(
            token = token,
            platform = "android"
        ),
        snapshot
    )
    .map {
        FcmTokenRegistration(it.registered)
    }

    override suspend fun unregisterToken(
        token: String,
        cleanup: com.teamnative.bookon.feature.auth.domain.SessionCleanupHandle
    ): NetworkResult<FcmTokenUnregistration> =
    remote.unregisterToken(
        token,
        cleanup
    ).map {
        FcmTokenUnregistration(it.unregistered)
    }

    override suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregistration> =
    remote.unregisterToken(token).map {
        FcmTokenUnregistration(it.unregistered)
    }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
