package com.teamnative.bookon.feature.auth.presentation.component

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookOnPasswordPolicyTest {
    @Test
    fun `6 to 15 character password with every required character group is valid`() {
        assertTrue(BookOnPasswordPolicy.isValid("Aa1!aa"))
        assertTrue(BookOnPasswordPolicy.isValid("Abcdef1!2345678"))
    }

    @Test
    fun `password outside length range or missing a required group is invalid`() {
        assertFalse(BookOnPasswordPolicy.isValid("Aa1!a"))
        assertFalse(BookOnPasswordPolicy.isValid("Abcdef1!23456789"))
        assertFalse(BookOnPasswordPolicy.isValid("abcdef1!"))
        assertFalse(BookOnPasswordPolicy.isValid("ABCDEF1!"))
        assertFalse(BookOnPasswordPolicy.isValid("Abcdefgh!"))
        assertFalse(BookOnPasswordPolicy.isValid("Abcdef12"))
    }

    @Test
    fun `password containing whitespace is invalid`() {
        assertFalse(BookOnPasswordPolicy.isValid("Aa1! ab"))
    }
}
