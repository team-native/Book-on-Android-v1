package com.teamnative.bookon.feature.media.domain

import com.teamnative.bookon.core.network.NetworkResult

interface ImageRepository {
    suspend fun image(file: String): NetworkResult<ImageFile>
}

data class ImageFile(val bytes: ByteArray, val contentType: String?)
