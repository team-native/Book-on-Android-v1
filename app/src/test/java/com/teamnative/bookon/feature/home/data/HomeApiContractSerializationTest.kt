package com.teamnative.bookon.feature.home.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeApiContractSerializationTest {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `실서버 홈 응답에서 현재 화면이 사용하는 추천 도서를 역직렬화한다`() {
        val responseBody = """
            {
              "errorCode": 0,
              "message": "메인 화면 조회 성공",
              "data": {
                "banners": [],
                "todayRecommendation": {
                  "bookId": 8013595087,
                  "title": "소프트웨어 공학",
                  "author": "저자",
                  "publisher": "출판사",
                  "category": "총류",
                  "libraryNumber": "004",
                  "coverImageUrl": null,
                  "loanAvailable": true,
                  "status": "대출가능",
                  "reason": "대출 통계 기반 추천"
                },
                "menus": [],
                "externalServices": {"dls": {"status": "OK"}}
              }
            }
        """.trimIndent()

        val envelope = json.decodeFromString<ApiEnvelope<HomeDto>>(responseBody)
        val recommendation = requireNotNull(envelope.data?.todayRecommendation)

        assertEquals(8013595087L, recommendation.bookId)
        assertEquals("대출 통계 기반 추천", recommendation.reason)
    }
}
