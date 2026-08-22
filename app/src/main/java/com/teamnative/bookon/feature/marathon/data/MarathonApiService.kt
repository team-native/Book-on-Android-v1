package com.teamnative.bookon.feature.marathon.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET

/** 로그인 사용자의 read365 연동 상태를 조회하는 HTTP 계약이다. */
interface MarathonApiService {
    @GET("marathon/read365/myinfo")
    suspend fun myInfo(): Response<ApiEnvelope<Read365MyInfoDto>>
}

@Serializable
data class Read365MyInfoDto(
    @SerialName("read365Id")
    val read365Id: String,
    @SerialName("memberKey")
    val memberKey: String? = null,
    @SerialName("schoolKey")
    val schoolKey: String? = null,
)
