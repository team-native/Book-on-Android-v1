package com.teamnative.bookon.feature.auth.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.Read365Repository
import com.teamnative.bookon.feature.auth.domain.Read365Session
import javax.inject.Inject

class Read365RepositoryImpl @Inject constructor(
    private val remote: Read365RemoteDataSource,
) : Read365Repository {
    override suspend fun login(id: String, password: String): NetworkResult<Read365Session> =
        remote.login(id, password).map { it.toDomain() }

    override suspend fun registerSession(
        cookieHeader: String,
        read365Id: String?,
        sessionExpiresAt: String?,
    ): NetworkResult<Read365Session> = remote.registerSession(
        Read365SessionRequestDto(cookieHeader, read365Id, sessionExpiresAt),
    ).map { it.toDomain() }

    override suspend fun extendSession(): NetworkResult<Read365Session> =
        remote.extendSession().map { Read365Session(it.read365Id, sessionExpiresAt = it.sessionExpiresAt) }
}

private fun Read365LoginResponseDto.toDomain() = Read365Session(
    read365Id = read365Id,
    cookie = cookie,
    sessionExpiresAt = sessionExpiresAt,
    jsessionId = jsessionId,
)

private fun Read365SessionResponseDto.toDomain() = Read365Session(
    read365Id = read365Id,
    sessionExpiresAt = sessionExpiresAt,
)

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
