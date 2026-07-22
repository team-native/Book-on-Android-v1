package com.teamnative.bookon.feature.home.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/** 공개 홈·공지 HTTP 계약이다. */
interface HomeApiService {
    @GET("home")
    suspend fun home(
        @Query("limit") limit: Int,
    ): Response<ApiEnvelope<HomeDto>>

    @GET("notices")
    suspend fun notices(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiEnvelope<NoticePageDto>>
}

@Serializable
data class HomeDto(
    @SerialName("todayRecommendation")
    val todayRecommendation: HomeBookDto? = null,
)

@Serializable
data class HomeBookDto(
    @SerialName("bookId")
    val bookId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val author: String,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("reason")
    val reason: String? = null,
)

@Serializable
data class NoticePageDto(
    @SerialName("items")
    val items: List<NoticeDto>,
)

@Serializable
data class NoticeDto(
    @SerialName("noticeId")
    val noticeId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("summary")
    val summary: String,
    @SerialName("createdAt")
    val createdAt: String,
)
