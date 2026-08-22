package com.teamnative.bookon.feature.media.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/** 프로필 이미지 등 서버 파일을 원본 바이트로 조회하는 HTTP 계약이다. */
interface ImageApiService {
    @GET("image/{file}")
    suspend fun image(
        @Path("file", encoded = true) file: String,
    ): Response<ResponseBody>
}
