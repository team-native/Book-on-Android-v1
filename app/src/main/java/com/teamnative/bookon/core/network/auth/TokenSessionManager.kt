package com.teamnative.bookon.core.network.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** 저장소와 메모리의 세션 전환을 직렬화하고 이전 요청의 변경을 거부한다. */
@Singleton
class TokenSessionManager @Inject constructor(
    private val tokenDataStore: DataStore<Preferences>,
    private val tokenCipher: TokenEncryption,
) {
    private val sessionMutex = Mutex()
    private val mutableSnapshot = MutableStateFlow(SessionSnapshot(
            java.security.SecureRandom().nextLong(),
            null
    ))
    val snapshot: StateFlow<SessionSnapshot> = mutableSnapshot.asStateFlow()
    private val mutableTokens = MutableStateFlow<AuthTokens?>(null)
    val tokens: StateFlow<AuthTokens?> = mutableTokens.asStateFlow()
    private var hasRestored = false
    @Volatile
    private var cleanupBarrier: CompletableDeferred<Unit>? = null

    suspend fun restore(): AuthTokens? = sessionMutex.withLock {
        if (hasRestored) {
            return@withLock mutableSnapshot.value.tokens
        }
        val preferences = tokenDataStore.data.catch { exception ->

            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()
        val accessToken = preferences[AccessTokenKey]?.let(tokenCipher::decrypt)
        val refreshToken = preferences[RefreshTokenKey]?.let(tokenCipher::decrypt)
        val restoredTokens = if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            null
        } else {
            AuthTokens(
                accessToken,
                refreshToken
            )
        }
        if (restoredTokens == null) {
            persist(null)
        }
        hasRestored = true
        publish(
            mutableSnapshot.value.epoch + 1L,
            restoredTokens
        )
        restoredTokens
    }

    suspend fun save(tokens: AuthTokens) = sessionMutex.withLock {
        persist(tokens)
        hasRestored = true
        publish(
            mutableSnapshot.value.epoch + 1L,
            tokens
        )
    }

    suspend fun saveIfCurrent(
        expected: SessionSnapshot,
        tokens: AuthTokens
    ): Boolean = sessionMutex.withLock {
        if (!isCurrent(expected)) {
            return@withLock false
        }
        persist(tokens)
        publish(
            expected.epoch,
            tokens
        )
        true
    }

    suspend fun loginIfCurrent(
        expected: SessionSnapshot,
        tokens: AuthTokens
    ): Boolean = sessionMutex.withLock {
        if (!isCurrent(expected)) {
            return@withLock false
        }
        persist(tokens)
        hasRestored = true
        publish(
            expected.epoch + 1L,
            tokens
        )
        true
    }

    suspend fun clear() = sessionMutex.withLock {
        persist(null)
        hasRestored = true
        publish(
            mutableSnapshot.value.epoch + 1L,
            null
        )
    }

    suspend fun clearIfCurrent(expected: SessionSnapshot): Boolean = sessionMutex.withLock {
        if (!isCurrent(expected)) {
            return@withLock false
        }
        persist(null)
        publish(
            expected.epoch + 1L,
            null
        )
        true
    }

    /** 로컬 로그아웃을 완료한 뒤 제한된 서버 정리에 쓸 핸들만 반환한다. */
    suspend fun beginLogout(): SessionCleanupAuthorization = sessionMutex.withLock {
        val previousTokens = mutableSnapshot.value.tokens
        val previousBarrier = cleanupBarrier
        val completion = CompletableDeferred<Unit>()
        persist(null)
        hasRestored = true
        cleanupBarrier = completion
        publish(
            mutableSnapshot.value.epoch + 1L,
            null
        )
        SessionCleanupAuthorization(
            previousTokens,
            previousBarrier,
            completion
        )
    }

    suspend fun finishCleanup(cleanup: SessionCleanupAuthorization) = withContext(NonCancellable) {
        sessionMutex.withLock {
            cleanup.completion.complete(Unit)
            if (cleanupBarrier === cleanup.completion) {
                cleanupBarrier = null
            }
        }

    }

    suspend fun awaitCleanup() {
        cleanupBarrier?.await()
    }

    fun isCurrent(expected: SessionSnapshot): Boolean {
        val current = mutableSnapshot.value
        return current.epoch == expected.epoch && current.tokens == expected.tokens
    }

    fun accessToken(): String? = mutableSnapshot.value.tokens?.accessToken
    fun refreshToken(): String? = mutableSnapshot.value.tokens?.refreshToken

    private suspend fun persist(tokens: AuthTokens?) {
        try {
            tokenDataStore.edit { preferences ->

                if (tokens == null) {
                    preferences.remove(AccessTokenKey)
                    preferences.remove(RefreshTokenKey)
                } else {
                    preferences[AccessTokenKey] = tokenCipher.encrypt(tokens.accessToken)
                    preferences[RefreshTokenKey] = tokenCipher.encrypt(tokens.refreshToken)
                }
            }
        } catch (exception: IOException) {
            throw SessionStorageException(exception)
        } catch (exception: java.security.GeneralSecurityException) {
            throw SessionStorageException(exception)
        } catch (exception: java.security.ProviderException) {
            throw SessionStorageException(exception)
        }
    }

    private fun publish(
        epoch: Long,
        tokens: AuthTokens?
    ) {
        mutableSnapshot.value = SessionSnapshot(
            epoch,
            tokens
        )
        mutableTokens.value = tokens
    }

    private companion object {
        val AccessTokenKey = stringPreferencesKey("encrypted_access_token")
        val RefreshTokenKey = stringPreferencesKey("encrypted_refresh_token")
    }
}
