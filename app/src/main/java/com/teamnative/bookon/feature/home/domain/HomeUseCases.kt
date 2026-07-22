package com.teamnative.bookon.feature.home.domain

import javax.inject.Inject

class GetHomeUseCase @Inject constructor(private val repository: HomeRepository) {
    /** 홈 진입 시 오늘의 추천 데이터를 조회한다. */
    suspend operator fun invoke(limit: Int) = repository.home(limit)
}

class GetNoticesUseCase @Inject constructor(private val repository: HomeRepository) {
    /** 홈 상단에 보일 최신 공지 목록을 조회한다. */
    suspend operator fun invoke(page: Int, size: Int) = repository.notices(page, size)
}
