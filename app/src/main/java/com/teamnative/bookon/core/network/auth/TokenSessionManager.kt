package com.teamnative.bookon.core.network.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 인증 토큰의 암호화 저장과 즉시 읽기 가능한 메모리 사본을 함께 관리한다.
 * Interceptor는 메모리 사본만 읽으므로 DataStore를 blocking 호출하지 않는다.
 */
@Singleton
class TokenSessionManager @Inject constructor(
    private val tokenDataStore: DataStore<Preferences>,
    private val tokenCipher: TokenCipher,
) {
    private val mutableTokens = MutableStateFlow<AuthTokens?>(null)
    /** Interceptor는 이 값만 동기적으로 읽고 UI는 세션 전환을 관찰한다. */
    val tokens: StateFlow<AuthTokens?> = mutableTokens.asStateFlow()

    /** 앱 시작 시 암호화된 DataStore 값을 읽어 Interceptor용 메모리 캐시를 채운다. */
    suspend fun restore(): AuthTokens? {
        val preferences = tokenDataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }
            .first()
        val accessToken = preferences[AccessTokenKey]?.let(tokenCipher::decrypt)
        val refreshToken = preferences[RefreshTokenKey]?.let(tokenCipher::decrypt)
        val restoredTokens = if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            null
        } else {
            AuthTokens(accessToken = accessToken, refreshToken = refreshToken)
        }
        mutableTokens.value = restoredTokens
        if (restoredTokens == null && (preferences[AccessTokenKey] != null || preferences[RefreshTokenKey] != null)) {
            clear()
        }
        return restoredTokens
    }

    /** 로그인 또는 refresh 성공 시 토큰을 암호화해 저장하고 메모리 캐시도 원자적으로 갱신한다. */
    suspend fun save(tokens: AuthTokens) {
        tokenDataStore.edit { preferences ->
            preferences[AccessTokenKey] = tokenCipher.encrypt(tokens.accessToken)
            preferences[RefreshTokenKey] = tokenCipher.encrypt(tokens.refreshToken)
        }
        mutableTokens.value = tokens
    }

    /** 로그아웃 또는 refresh 실패 시 저장소와 메모리의 토큰을 함께 제거한다. */
    suspend fun clear() {
        tokenDataStore.edit { preferences ->
            preferences.remove(AccessTokenKey)
            preferences.remove(RefreshTokenKey)
        }
        mutableTokens.value = null
    }

    fun accessToken(): String? = mutableTokens.value?.accessToken
    fun refreshToken(): String? = mutableTokens.value?.refreshToken

    private companion object {
        val AccessTokenKey = stringPreferencesKey("encrypted_access_token")
        val RefreshTokenKey = stringPreferencesKey("encrypted_refresh_token")
    }
}
