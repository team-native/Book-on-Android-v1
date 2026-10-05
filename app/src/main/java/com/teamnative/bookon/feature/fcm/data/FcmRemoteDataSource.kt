package com.teamnative.bookon.feature.fcm.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface FcmRemoteDataSource {
    suspend fun registerToken(request: FcmTokenRegisterRequestDto): NetworkResult<FcmTokenRegisterResponseDto>
    suspend fun registerToken(
        request: FcmTokenRegisterRequestDto,
        snapshot: com.teamnative.bookon.core.network.auth.SessionSnapshot
    ): NetworkResult<FcmTokenRegisterResponseDto> = registerToken(request)
    suspend fun unregisterToken(
        token: String,
        cleanup: com.teamnative.bookon.feature.auth.domain.SessionCleanupHandle
    ): NetworkResult<FcmTokenUnregisterResponseDto> =
    NetworkResult.Failure(com.teamnative.bookon.core.network.NetworkError.EmptyBody("Cleanup unavailable"))
    suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregisterResponseDto>
}

class FcmRemoteDataSourceImpl @Inject constructor(
    private val api: FcmApiService,
    private val executor: ApiExecutor,
) : FcmRemoteDataSource {
    /** 현재 기기의 FCM 등록 토큰을 서버 계정에 연결한다. */
    override suspend fun registerToken(request: FcmTokenRegisterRequestDto) =
    executor.execute {
        api.registerToken(request)
    }

    override suspend fun registerToken(
        request: FcmTokenRegisterRequestDto,
        snapshot: com.teamnative.bookon.core.network.auth.SessionSnapshot
    ) =
    executor.execute {
        api.registerToken(
            request,
            snapshot
        )
    }

    override suspend fun unregisterToken(
        token: String,
        cleanup: com.teamnative.bookon.feature.auth.domain.SessionCleanupHandle
    ): NetworkResult<FcmTokenUnregisterResponseDto> {
        val credentials = cleanup as? com.teamnative.bookon.core.network.auth.SessionCleanupAuthorization
        ?: return NetworkResult.Failure(com.teamnative.bookon.core.network.NetworkError.EmptyBody("Cleanup unavailable"))
        return executor.execute {
            api.unregisterToken(
                FcmTokenUnregisterRequestDto(token),
                credentials
            )
        }
    }

    /** 현재 기기의 FCM 등록 토큰을 서버 계정에서 해제한다. */
    override suspend fun unregisterToken(token: String) =
    executor.execute {
        api.unregisterToken(FcmTokenUnregisterRequestDto(token))
    }
}
