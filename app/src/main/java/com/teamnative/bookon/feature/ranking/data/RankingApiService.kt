package com.teamnative.bookon.feature.ranking.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingApiService {
    @GET("rankings/readers")
    suspend fun readers(
        @Query("year") year: Int,
        @Query("limit") limit: Int,
    ): Response<ApiEnvelope<ReaderRankingDto>>
}

@Serializable
data class ReaderRankingDto(
    @SerialName("year")
    val year: Int,
    @SerialName("resetPolicy")
    val resetPolicy: String,
    @SerialName("items")
    val items: List<ReaderDto>,
)

@Serializable
data class ReaderDto(
    @SerialName("rank")
    val rank: Int,
    @SerialName("name")
    val name: String,
    @SerialName("department")
    val department: String,
    @SerialName("loanCount")
    val loanCount: Int,
)
