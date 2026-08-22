package com.teamnative.bookon.feature.school.data

import com.teamnative.bookon.core.network.ApiExecutor
import com.teamnative.bookon.core.network.NetworkResult
import javax.inject.Inject

interface SchoolRemoteDataSource {
    suspend fun search(keyword: String): NetworkResult<SchoolSearchResponseDto>
    suspend fun verifyStudent(schoolId: Long, studentNo: String, name: String): NetworkResult<StudentVerificationResponseDto>
}

class SchoolRemoteDataSourceImpl @Inject constructor(
    private val api: SchoolApiService,
    private val executor: ApiExecutor,
) : SchoolRemoteDataSource {
    /** 학교 검색어로 회원가입에 사용할 학교 목록을 조회한다. */
    override suspend fun search(keyword: String) = executor.execute { api.search(keyword) }

    /** 학교·학번·이름 조합의 재학 여부를 확인한다. */
    override suspend fun verifyStudent(schoolId: Long, studentNo: String, name: String) = executor.execute {
        api.verifyStudent(VerifyStudentRequestDto(schoolId, studentNo, name))
    }
}
