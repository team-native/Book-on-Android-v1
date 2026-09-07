package com.teamnative.bookon.feature.fcm.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface FcmRemoteDataSource {
    suspend fun registerToken(request: FcmTokenRegisterRequestDto): NetworkResult<FcmTokenRegisterResponseDto>
    suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregisterResponseDto>
}

class FcmRemoteDataSourceImpl @Inject constructor(
    private val api: FcmApiService,
    private val executor: ApiExecutor,
) : FcmRemoteDataSource {
    /** 현재 기기의 FCM 등록 토큰을 서버 계정에 연결한다. */
    override suspend fun registerToken(request: FcmTokenRegisterRequestDto) =
        executor.execute { api.registerToken(request) }

    /** 현재 기기의 FCM 등록 토큰을 서버 계정에서 해제한다. */
    override suspend fun unregisterToken(token: String) =
        executor.execute { api.unregisterToken(FcmTokenUnregisterRequestDto(token)) }
}
