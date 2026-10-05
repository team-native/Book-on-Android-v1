package com.teamnative.bookon.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiPagination(
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("hasNext") val hasNext: Boolean,
)
