package com.teamnative.bookon.feature.fcm.data

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FcmRepositoryImplTest {
    @Test
    fun `토큰 등록은 플랫폼 기본값 android를 사용하고 등록 상태를 도메인으로 변환한다`() = kotlinx.coroutines.test.runTest {
        val remoteDataSource = RecordingFcmRemoteDataSource()
        val repository = FcmRepositoryImpl(remoteDataSource)

        val result = repository.registerToken(token = "fcm-registration-token")

        assertEquals("android", remoteDataSource.registerRequest?.platform)
        assertTrue((result as NetworkResult.Success).data.registered)
    }

    @Test
    fun `토큰 해제 실패는 그대로 전달된다`() = kotlinx.coroutines.test.runTest {
        val remoteDataSource = RecordingFcmRemoteDataSource(
            unregisterResult = NetworkResult.Failure(NetworkError.Http(401, 4010, "인증이 필요합니다.")),
        )
        val repository = FcmRepositoryImpl(remoteDataSource)

        val result = repository.unregisterToken(token = "fcm-registration-token")

        assertTrue(result is NetworkResult.Failure)
    }
}

private class RecordingFcmRemoteDataSource(
    private val unregisterResult: NetworkResult<FcmTokenUnregisterResponseDto> =
        NetworkResult.Success(FcmTokenUnregisterResponseDto(unregistered = true)),
) : FcmRemoteDataSource {
    var registerRequest: FcmTokenRegisterRequestDto? = null

    override suspend fun registerToken(request: FcmTokenRegisterRequestDto): NetworkResult<FcmTokenRegisterResponseDto> {
        registerRequest = request
        return NetworkResult.Success(FcmTokenRegisterResponseDto(registered = true))
    }

    override suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregisterResponseDto> = unregisterResult
}
