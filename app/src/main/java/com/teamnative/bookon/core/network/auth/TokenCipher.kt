package com.teamnative.bookon.core.network.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/** Android Keystore 키로 DataStore에 저장할 토큰 문자열을 AES-GCM 암호화한다. */
@Singleton
class TokenCipher @Inject constructor() {
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(Transformation)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey())
        val encrypted = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return "${encode(cipher.iv)}:${encode(encrypted)}"
    }

    fun decrypt(cipherText: String): String? = runCatching {
        val (initializationVector, encrypted) = cipherText.split(':', limit = 2)
        val cipher = Cipher.getInstance(Transformation)
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey(),
            GCMParameterSpec(GcmTagLengthBits, decode(initializationVector)),
        )
        String(cipher.doFinal(decode(encrypted)), StandardCharsets.UTF_8)
    }.getOrNull()

    private fun secretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(AndroidKeyStore).apply { load(null) }
        (keyStore.getKey(KeyAlias, null) as? SecretKey)?.let { return it }
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(KeyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build(),
        )
        return keyGenerator.generateKey()
    }

    private fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)
    private fun decode(value: String): ByteArray = Base64.decode(value, Base64.NO_WRAP)

    private companion object {
        const val AndroidKeyStore = "AndroidKeyStore"
        const val KeyAlias = "bookon_auth_token_key"
        const val Transformation = "AES/GCM/NoPadding"
        const val GcmTagLengthBits = 128
    }
}
