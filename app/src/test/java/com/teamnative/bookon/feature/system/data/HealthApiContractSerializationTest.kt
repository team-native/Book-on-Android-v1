package com.teamnative.bookon.feature.system.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class HealthApiContractSerializationTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `실서버 health 원시 응답을 역직렬화한다`() {
        val healthResponse = json.decodeFromString<HealthDto>("""{"status":"ok"}""")

        assertEquals("ok", healthResponse.status)
    }
}
