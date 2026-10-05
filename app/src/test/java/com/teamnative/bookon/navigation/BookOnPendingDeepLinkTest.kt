package com.teamnative.bookon.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BookOnPendingDeepLinkTest {
    @Test
    fun `loan_due 타입은 대출 내역 화면으로 매핑된다`() {
        assertEquals(BookOnDestination.LoanHistory, "loan_due".toPendingDeepLinkDestination())
    }

    @Test
    fun `notice 타입은 공지 목록으로 이동한다`() {
        assertEquals(BookOnDestination.Notices, "notice".toPendingDeepLinkDestination())
    }

    @Test
    fun `new book requires positive identifier`() {
        assertNull("new_book".toPendingDeepLinkDestination())
        assertNull("new_book".toPendingDeepLinkDestination(0))
        assertNull("new_book".toPendingDeepLinkDestination(-1))
        assertEquals(BookOnDestination.BookDetail(17), "new_book".toPendingDeepLinkDestination(17))
        assertNull("https://example.com".toPendingDeepLinkDestination(17))
    }

    @Test
    fun `null이나 알 수 없는 타입은 null로 수렴한다`() {
        assertNull(null.toPendingDeepLinkDestination())
        assertNull("unknown_type".toPendingDeepLinkDestination())
    }
}
