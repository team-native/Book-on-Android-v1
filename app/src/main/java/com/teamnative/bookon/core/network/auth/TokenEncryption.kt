package com.teamnative.bookon.core.network.auth

interface TokenEncryption {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String?
}
