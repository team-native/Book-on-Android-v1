package com.teamnative.bookon.feature.school.domain

import com.teamnative.bookon.core.network.NetworkResult

interface SchoolRepository {
    suspend fun search(keyword: String): NetworkResult<List<School>>
    suspend fun verifyStudent(schoolId: Long, studentNo: String, name: String): NetworkResult<StudentVerification>
}

data class School(val schoolId: Long, val schoolName: String, val region: String)
data class StudentVerification(
    val verified: Boolean,
    val userKey: String?,
    val userNo: String?,
    val school: School,
)
