package com.teamnative.bookon.core.network

import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import retrofit2.Response

/**
 * RemoteDataSource의 HTTP 호출을 공통 결과로 정규화한다.
 * 취소는 호출자 생명주기로 전파하고 HTTP·IO·직렬화 오류만 NetworkResult로 변환한다.
 */
@Singleton
class ApiExecutor @Inject constructor(
    private val json: Json,
) {
    suspend fun <T> execute(
        request: suspend () -> Response<ApiEnvelope<T>>,
    ): NetworkResult<T> = try {
        val response = request()
        val body = response.body()
        when {
            response.isSuccessful && body?.errorCode == 0 && body.data != null -> {
                NetworkResult.Success(body.data)
            }
            response.isSuccessful && body != null -> {
                NetworkResult.Failure(
                    NetworkError.Http(
                        statusCode = response.code(),
                        errorCode = body.errorCode,
                        message = body.message,
                    ),
                )
            }
            else -> {
                val error = response.errorBody()?.string()?.let { raw ->
                    runCatching { json.decodeFromString<ApiEnvelope<JsonElement>>(raw) }.getOrNull()
                }
                NetworkResult.Failure(NetworkError.Http(response.code(), error?.errorCode, error?.message ?: response.message()))
            }
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: SerializationException) {
        NetworkResult.Failure(NetworkError.Serialization(exception))
    } catch (exception: IOException) {
        NetworkResult.Failure(NetworkError.Network(exception))
    }

    /** ApiEnvelope를 사용하지 않는 헬스 체크 같은 원시 JSON 응답을 공통 결과로 변환한다. */
    suspend fun <T> executeRaw(
        request: suspend () -> Response<T>,
    ): NetworkResult<T> = try {
        val response = request()
        val responseBody = response.body()
        when {
            response.isSuccessful && responseBody != null -> NetworkResult.Success(responseBody)
            response.isSuccessful -> NetworkResult.Failure(
                NetworkError.EmptyBody("서버 응답 본문이 비어 있습니다."),
            )
            else -> {
                val errorEnvelope = response.errorBody()?.string()?.let { rawResponse ->
                    runCatching {
                        json.decodeFromString<ApiEnvelope<JsonElement>>(rawResponse)
                    }.getOrNull()
                }
                NetworkResult.Failure(
                    NetworkError.Http(
                        statusCode = response.code(),
                        errorCode = errorEnvelope?.errorCode,
                        message = errorEnvelope?.message ?: response.message(),
                    ),
                )
            }
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: SerializationException) {
        NetworkResult.Failure(NetworkError.Serialization(exception))
    } catch (exception: IOException) {
        NetworkResult.Failure(NetworkError.Network(exception))
    }

    /** 성공 응답의 data가 null인 명령형 API를 Unit 결과로 정규화한다. */
    suspend fun executeUnit(
        request: suspend () -> Response<ApiEnvelope<EmptyResponseDto>>,
    ): NetworkResult<Unit> = try {
        val response = request()
        val body = response.body()
        when {
            response.isSuccessful && body?.errorCode == 0 -> NetworkResult.Success(Unit)
            response.isSuccessful && body != null -> NetworkResult.Failure(
                NetworkError.Http(response.code(), body.errorCode, body.message),
            )
            else -> {
                val error = response.errorBody()?.string()?.let { raw ->
                    runCatching { json.decodeFromString<ApiEnvelope<JsonElement>>(raw) }.getOrNull()
                }
                NetworkResult.Failure(
                    NetworkError.Http(response.code(), error?.errorCode, error?.message ?: response.message()),
                )
            }
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: SerializationException) {
        NetworkResult.Failure(NetworkError.Serialization(exception))
    } catch (exception: IOException) {
        NetworkResult.Failure(NetworkError.Network(exception))
    }
}

/** data가 null인 성공 응답을 역직렬화하기 위한 명시적 DTO다. */
@kotlinx.serialization.Serializable
class EmptyResponseDto
