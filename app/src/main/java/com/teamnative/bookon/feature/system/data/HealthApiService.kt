package com.teamnative.bookon.feature.system.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET

interface HealthApiService {
    @GET("life")
    suspend fun health(): Response<ApiEnvelope<HealthDto>>
}

@Serializable
data class HealthDto(
    @SerialName("status") val status: String,
)
