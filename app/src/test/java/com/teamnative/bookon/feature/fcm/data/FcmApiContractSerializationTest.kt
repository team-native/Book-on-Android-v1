package com.teamnative.bookon.feature.fcm.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class FcmApiContractSerializationTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `실서버 FCM 토큰 등록 응답을 역직렬화한다`() {
        val responseBody = """
            {
              "errorCode": 0,
              "message": "FCM 토큰이 등록되었습니다.",
              "data": { "registered": true }
            }
        """.trimIndent()

        val envelope = json.decodeFromString<ApiEnvelope<FcmTokenRegisterResponseDto>>(responseBody)

        assertEquals(true, envelope.data?.registered)
    }

    @Test
    fun `실서버 FCM 토큰 해제 응답을 역직렬화한다`() {
        val responseBody = """
            {
              "errorCode": 0,
              "message": "FCM 토큰이 해제되었습니다.",
              "data": { "unregistered": true }
            }
        """.trimIndent()

        val envelope = json.decodeFromString<ApiEnvelope<FcmTokenUnregisterResponseDto>>(responseBody)

        assertEquals(true, envelope.data?.unregistered)
    }

    @Test
    fun `FCM 토큰 등록 요청은 서버 필드명 그대로 직렬화된다`() {
        val request = FcmTokenRegisterRequestDto(
            token = "fcm-registration-token",
            dlsUserKey = "123456",
            platform = "android",
            deviceId = "device-uuid",
        )

        val serialized = json.encodeToString(FcmTokenRegisterRequestDto.serializer(), request)

        assertEquals(
            """{"token":"fcm-registration-token","dlsUserKey":"123456","platform":"android","deviceId":"device-uuid"}""",
            serialized,
        )
    }
}
