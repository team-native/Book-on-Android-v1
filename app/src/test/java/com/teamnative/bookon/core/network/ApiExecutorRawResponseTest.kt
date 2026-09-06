package com.teamnative.bookon.core.network

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ApiExecutorRawResponseTest {
    private val executor = ApiExecutor(
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        },
    )

    @Test
    fun `원시 JSON 성공 응답의 본문을 반환한다`() = runTest {
        val responseBody = RawStatus("ok")

        val networkResult = executor.executeRaw { Response.success(responseBody) }

        assertEquals(responseBody, (networkResult as NetworkResult.Success).data)
    }

    @Test
    fun `원시 응답 HTTP 실패에서도 공통 오류 형식을 해석한다`() = runTest {
        val networkResult = executor.executeRaw<RawStatus> {
            Response.error(
                404,
                """{"errorCode":4040,"message":"요청한 API를 찾을 수 없습니다.","data":null}"""
                    .toResponseBody(),
            )
        }

        assertTrue(networkResult is NetworkResult.Failure)
        val networkError = (networkResult as NetworkResult.Failure).error as NetworkError.Http
        assertEquals(4040, networkError.errorCode)
        assertEquals("요청한 API를 찾을 수 없습니다.", networkError.message)
    }

    private data class RawStatus(val status: String)
}
