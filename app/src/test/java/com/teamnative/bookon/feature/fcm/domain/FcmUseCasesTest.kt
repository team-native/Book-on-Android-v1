package com.teamnative.bookon.feature.fcm.domain

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.notification.FcmTokenProvider
import org.junit.Assert.assertFalse
import org.junit.Test

class FcmUseCasesTest {
    @Test
    fun `기기 토큰이 없으면 등록을 서버에 요청하지 않는다`() = kotlinx.coroutines.test.runTest {
        val fcmTokenProvider = FakeFcmTokenProvider(token = null)
        val repository = RecordingFcmRepository()
        val useCase = SyncFcmTokenOnAuthenticationUseCase(fcmTokenProvider, RegisterFcmTokenUseCase(repository))

        useCase()

        assertFalse(repository.registerInvoked)
    }

    @Test
    fun `기기 토큰이 없으면 해제를 서버에 요청하지 않는다`() = kotlinx.coroutines.test.runTest {
        val fcmTokenProvider = FakeFcmTokenProvider(token = null)
        val repository = RecordingFcmRepository()
        val useCase = ClearFcmTokenOnLogoutUseCase(fcmTokenProvider, UnregisterFcmTokenUseCase(repository))

        useCase()

        assertFalse(repository.unregisterInvoked)
    }
}

private class FakeFcmTokenProvider(private val token: String?) : FcmTokenProvider {
    override suspend fun currentToken(): String? = token
}

private class RecordingFcmRepository : FcmRepository {
    var registerInvoked = false
        private set
    var unregisterInvoked = false
        private set

    override suspend fun registerToken(token: String, platform: String): NetworkResult<FcmTokenRegistration> {
        registerInvoked = true
        return NetworkResult.Success(FcmTokenRegistration(registered = true))
    }

    override suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregistration> {
        unregisterInvoked = true
        return NetworkResult.Success(FcmTokenUnregistration(unregistered = true))
    }
}
