package com.teamnative.bookon.core.network

import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.Response

/**
 * RemoteDataSource의 HTTP 호출을 공통 결과로 정규화한다.
 * 취소는 호출자 생명주기로 전파하고 HTTP·IO·직렬화 오류만 NetworkResult로 변환한다.
 */
@Singleton
class ApiExecutor @Inject constructor() {
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
            else -> NetworkResult.Failure(
                NetworkError.Http(
                    statusCode = response.code(),
                    errorCode = null,
                    message = response.message(),
                ),
            )
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: SerializationException) {
        NetworkResult.Failure(NetworkError.Serialization(exception))
    } catch (exception: IOException) {
        NetworkResult.Failure(NetworkError.Network(exception))
    }
}
