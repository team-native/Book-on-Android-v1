package com.teamnative.bookon.feature.book.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookApiContractSerializationTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    @Test
    fun `실서버 도서 목록 응답을 현재 DTO로 역직렬화한다`() {
        val responseBody =
            """
            {
              "errorCode": 0,
              "message": "도서 목록 조회 성공",
              "data": {
                "items": [{
                  "bookId": 8013595087,
                  "title": "소프트웨어 공학",
                  "author": "저자",
                  "publisher": "출판사",
                  "category": "총류",
                  "libraryNumber": "004",
                  "coverImageUrl": null,
                  "totalQuantity": 1,
                  "availableQuantity": 1,
                  "loanAvailable": true,
                  "status": "대출가능",
                  "isbn": "9791132100768",
                  "registeredAt": "2019-12-25"
                }],
                "pagination": {
                  "page": 1,
                  "size": 1,
                  "totalCount": 29,
                  "totalPages": 29,
                  "hasNext": true
                }
              }
            }
            """.trimIndent()

        val envelope = json.decodeFromString<ApiEnvelope<BookPageDto>>(responseBody)
        val bookPage = requireNotNull(envelope.data)

        assertEquals(8013595087L, bookPage.items.single().bookId)
        assertTrue(bookPage.items.single().loanAvailable)
        assertTrue(bookPage.pagination.hasNext)
    }

    @Test
    fun `상세 nullable 상태와 수량을 수신하고 누락 수량은 보존한다`() {
        val base = """{
            "bookId":8013595087,"title":"책","author":"저자",
            "publisher":"출판사","category":"분류","libraryNumber":"813",
            "loanAvailable":false,"status":null
        }"""
        val missing = json.decodeFromString<BookDetailDto>(base)
        assertEquals(null, missing.status)
        assertEquals(null, missing.totalQuantity)
        assertEquals(null, missing.availableQuantity)
        val stocked =
            json.decodeFromString<BookDetailDto>(
                base.trimEnd().dropLast(1) + ",\"totalQuantity\":3,\"availableQuantity\":0}",
            )
        assertEquals(3, stocked.totalQuantity)
        assertEquals(0, stocked.availableQuantity)
    }
}
