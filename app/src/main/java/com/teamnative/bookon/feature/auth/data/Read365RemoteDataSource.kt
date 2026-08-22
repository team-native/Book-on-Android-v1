package com.teamnative.bookon.feature.auth.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

/** Read365 인증 세션 등록·연장 API를 호출하는 RemoteDataSource다. */
interface Read365RemoteDataSource {
    suspend fun login(id: String, password: String): NetworkResult<Read365LoginResponseDto>
    suspend fun registerSession(request: Read365SessionRequestDto): NetworkResult<Read365SessionResponseDto>
    suspend fun extendSession(): NetworkResult<Read365SessionResponseDto>
}

class Read365RemoteDataSourceImpl @Inject constructor(
    private val api: Read365ApiService,
    private val executor: ApiExecutor,
) : Read365RemoteDataSource {
    /** Read365 ID/PW로 세션을 발급받고, 서버가 반환한 명시 필드만 전달한다. */
    override suspend fun login(id: String, password: String) = executor.execute {
        api.linkRead365(Read365LoginRequestDto(id, password))
    }

    /** WebView 등에서 얻은 Cookie 세션을 서버에 등록한다. */
    override suspend fun registerSession(request: Read365SessionRequestDto) = executor.execute {
        api.registerRead365Session(request)
    }

    /** 서버에 저장된 Read365 세션의 연장을 요청한다. */
    override suspend fun extendSession() = executor.execute {
        api.extendRead365Session()
    }
}
