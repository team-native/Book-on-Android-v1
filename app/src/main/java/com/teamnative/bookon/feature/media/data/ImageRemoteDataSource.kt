package com.teamnative.bookon.feature.media.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject
import okhttp3.ResponseBody

interface ImageRemoteDataSource {
    suspend fun image(file: String): NetworkResult<RemoteImageDto>
}

class ImageRemoteDataSourceImpl @Inject constructor(
    private val api: ImageApiService,
    private val executor: ApiExecutor,
) : ImageRemoteDataSource {
    /** 이미지 응답 스트림을 요청 범위의 바이트로 변환하고, 스트림 오류를 네트워크 오류로 전달한다. */
    override suspend fun image(file: String): NetworkResult<RemoteImageDto> = when (
        val result = executor.executeRaw { api.image(file) }
    ) {
        is NetworkResult.Success -> result.data.use { responseBody -> responseBody.readBounded() }
        is NetworkResult.Failure -> result
    }
}

/** 서버가 비정상적으로 큰 파일을 내려도 앱 메모리에 무제한으로 적재하지 않는다. */
private fun ResponseBody.readBounded(): NetworkResult<RemoteImageDto> {
    if (contentLength() > MaxImageBytes) {
        return NetworkResult.Failure(
            NetworkError.Http(413, null, "이미지 응답이 허용 크기를 초과했습니다."),
        )
    }

    return try {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(ReadBufferBytes)
        byteStream().use { input ->
            while (true) {
                val readBytes = input.read(buffer)
                if (readBytes == -1) break
                if (output.size() + readBytes > MaxImageBytes) {
                    return NetworkResult.Failure(
                        NetworkError.Http(413, null, "이미지 응답이 허용 크기를 초과했습니다."),
                    )
                }
                output.write(buffer, 0, readBytes)
            }
        }
        NetworkResult.Success(RemoteImageDto(output.toByteArray(), contentType()?.toString()))
    } catch (exception: IOException) {
        NetworkResult.Failure(NetworkError.Network(exception))
    }
}

private const val MaxImageBytes = 5 * 1024 * 1024
private const val ReadBufferBytes = 8 * 1024

data class RemoteImageDto(
    val bytes: ByteArray,
    val contentType: String?,
)
