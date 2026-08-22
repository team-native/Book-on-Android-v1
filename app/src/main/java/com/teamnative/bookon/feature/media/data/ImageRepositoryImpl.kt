package com.teamnative.bookon.feature.media.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.media.domain.ImageFile
import com.teamnative.bookon.feature.media.domain.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val remote: ImageRemoteDataSource,
) : ImageRepository {
    override suspend fun image(file: String): NetworkResult<ImageFile> = remote.image(file).map {
        ImageFile(it.bytes, it.contentType)
    }
}

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
