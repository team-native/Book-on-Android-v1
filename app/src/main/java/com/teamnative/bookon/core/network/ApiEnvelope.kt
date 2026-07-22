package com.teamnative.bookon.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** 백엔드가 성공·실패 응답에 공통으로 사용하는 JSON 포맷이다. */
@Serializable
data class ApiEnvelope<T>(
    @SerialName("errorCode")
    val errorCode: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
)
