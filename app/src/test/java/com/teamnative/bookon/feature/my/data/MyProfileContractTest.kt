package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.feature.my.domain.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class MyProfileContractTest {
    @Test
    fun `DLS identifiers and nullable loan fields decode`() {
        val loan = Json.decodeFromString<UserLoanDto>("""{"loanId":"DLS-1","bookId":null,"title":"book","dueDate":null,"dDay":null,"extensionAvailable":false}""")
        assertEquals("DLS-1", loan.loanId.content)
        assertNull(loan.bookId)
        assertNull(loan.dDay)
        val local = Json.decodeFromString<UserLoanDto>("""{"loanId":12,"title":"book","extensionAvailable":false}""")
        assertEquals("12", local.loanId.content)
        assertNull(Json.decodeFromString<LoanSummaryDto>("""{"currentLoanCount":0,"overdueCount":0}""").totalLoanCount)
    }

    @Test
    fun `due soon uses server zero through three and preserves unknown`() {
        val profile = MyProfile("name", "department", null, 4, 1, null,
            NotificationSettings(true, true, true),
            listOf(-1, 0, 3, 4).map { MyCurrentLoanSummary(it, null) },
        )
        assertEquals(2, profile.dueSoonCount)
        assertNull(profile.copy(currentLoans = profile.currentLoans + MyCurrentLoanSummary(null, null), currentLoanCount = 5).dueSoonCount)
        assertNull(profile.copy(currentLoanCount = 5).dueSoonCount)
        assertEquals(0, profile.copy(currentLoanCount = 0, currentLoans = emptyList()).dueSoonCount)
    }
}
