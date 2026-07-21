package com.teamnative.bookon.core.network

import kotlinx.serialization.Serializable

/** 백엔드가 성공·실패 응답에 공통으로 사용하는 JSON 포맷이다. */
@Serializable
data class ApiEnvelope<T>(
    val errorCode: Int,
    val message: String,
    val data: T? = null,
)
