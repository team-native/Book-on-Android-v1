package com.teamnative.bookon.feature.school.data

import com.teamnative.bookon.core.network.ApiEnvelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SchoolApiService {
    @GET("schools/search")
    suspend fun search(
        @Query("keyword") keyword: String,
    ): Response<ApiEnvelope<SchoolSearchResponseDto>>

    @POST("auth/school/verify-student")
    suspend fun verifyStudent(
        @Body body: VerifyStudentRequestDto,
    ): Response<ApiEnvelope<StudentVerificationResponseDto>>
}

@Serializable
data class SchoolSearchResponseDto(
    @SerialName("items") val items: List<SchoolDto>,
)

@Serializable
data class SchoolDto(
    @SerialName("schoolId") val schoolId: Long,
    @SerialName("schoolName") val schoolName: String,
    @SerialName("region") val region: String,
)

@Serializable
data class VerifyStudentRequestDto(
    @SerialName("schoolId") val schoolId: Long,
    @SerialName("studentNo") val studentNo: String,
    @SerialName("name") val name: String,
)

@Serializable
data class StudentVerificationResponseDto(
    @SerialName("verified") val verified: Boolean,
    @SerialName("userKey") val userKey: String? = null,
    @SerialName("userNo") val userNo: String? = null,
    @SerialName("school") val school: SchoolDto,
)
