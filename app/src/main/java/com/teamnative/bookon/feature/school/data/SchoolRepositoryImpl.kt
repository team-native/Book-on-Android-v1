package com.teamnative.bookon.feature.school.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.school.domain.School
import com.teamnative.bookon.feature.school.domain.SchoolRepository
import com.teamnative.bookon.feature.school.domain.StudentVerification
import javax.inject.Inject

class SchoolRepositoryImpl @Inject constructor(
    private val remote: SchoolRemoteDataSource,
) : SchoolRepository {
    override suspend fun search(keyword: String): NetworkResult<List<School>> = remote.search(keyword).map {
        it.items.map { school -> school.toDomain() }
    }

    override suspend fun verifyStudent(
        schoolId: Long,
        studentNo: String,
        name: String,
    ): NetworkResult<StudentVerification> = remote.verifyStudent(schoolId, studentNo, name).map {
        StudentVerification(it.verified, it.userKey, it.userNo, it.school.toDomain())
    }
}

private fun SchoolDto.toDomain() = School(schoolId, schoolName, region)

private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
