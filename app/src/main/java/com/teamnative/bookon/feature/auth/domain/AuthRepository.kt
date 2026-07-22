package com.teamnative.bookon.feature.auth.domain

import com.teamnative.bookon.core.network.NetworkResult

interface AuthRepository {
    suspend fun register(request: RegistrationDraft): NetworkResult<RegistrationSession>
    suspend fun verifyRegistration(sessionId: String, passcode: String): NetworkResult<RegisteredUser>
    suspend fun login(loginId: String, password: String): NetworkResult<LoginSession>
    suspend fun logout(refreshToken: String): NetworkResult<Unit>
    suspend fun sendPasswordResetEmail(email: String): NetworkResult<Unit>
    suspend fun resetPassword(email: String, code: String, password: String, passwordConfirm: String): NetworkResult<Unit>
    suspend fun linkRead365(id: String, password: String): NetworkResult<Unit>
}

data class RegistrationDraft(
    val email: String,
    val name: String,
    val department: String,
    val gender: String,
    val password: String,
    val passwordConfirm: String,
)
data class RegistrationSession(val sessionId: String, val expiresAt: String, val email: String)
data class RegisteredUser(val userId: Long, val email: String, val name: String)

data class LoginSession(val accessToken: String, val refreshToken: String)
