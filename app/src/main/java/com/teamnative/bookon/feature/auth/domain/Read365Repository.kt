package com.teamnative.bookon.feature.auth.domain

import com.teamnative.bookon.core.network.NetworkResult

interface Read365Repository {
    suspend fun login(id: String, password: String): NetworkResult<Read365Session>
    suspend fun registerSession(
        cookieHeader: String,
        read365Id: String? = null,
        sessionExpiresAt: String? = null,
    ): NetworkResult<Read365Session>

    suspend fun extendSession(): NetworkResult<Read365Session>
}

data class Read365Session(
    val read365Id: String,
    val cookie: String? = null,
    val sessionExpiresAt: String? = null,
    val jsessionId: String? = null,
)
