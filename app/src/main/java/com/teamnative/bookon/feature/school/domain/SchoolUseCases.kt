package com.teamnative.bookon.feature.school.domain

import javax.inject.Inject

class SearchSchoolsUseCase @Inject constructor(
    private val repository: SchoolRepository,
) {
    /** 회원가입 과정에서 학교 이름 또는 지역으로 학교를 검색한다. */
    suspend operator fun invoke(keyword: String) = repository.search(keyword)
}

class VerifyStudentUseCase @Inject constructor(
    private val repository: SchoolRepository,
) {
    /** 선택한 학교의 학번과 이름으로 재학 여부를 검증한다. */
    suspend operator fun invoke(schoolId: Long, studentNo: String, name: String) =
        repository.verifyStudent(schoolId, studentNo, name)
}
