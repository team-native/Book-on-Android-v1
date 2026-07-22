package com.teamnative.bookon.core.network

/** 데이터 계층이 Domain 계층에 노출하는 공통 네트워크 호출 결과다. */
sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Failure(val error: NetworkError) : NetworkResult<Nothing>
}

sealed interface NetworkError {
    data class Http(val statusCode: Int, val errorCode: Int?, val message: String) : NetworkError
    data class Network(val cause: Throwable) : NetworkError
    data class Serialization(val cause: Throwable) : NetworkError
    data class EmptyBody(val message: String) : NetworkError
}
