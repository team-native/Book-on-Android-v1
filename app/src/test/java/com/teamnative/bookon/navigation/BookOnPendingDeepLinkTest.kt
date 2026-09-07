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
    fun `notice 타입은 아직 대응 화면이 없어 null로 수렴한다`() {
        assertNull("notice".toPendingDeepLinkDestination())
    }

    @Test
    fun `null이나 알 수 없는 타입은 null로 수렴한다`() {
        assertNull(null.toPendingDeepLinkDestination())
        assertNull("unknown_type".toPendingDeepLinkDestination())
    }
}
