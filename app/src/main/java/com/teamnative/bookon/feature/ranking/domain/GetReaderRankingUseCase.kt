package com.teamnative.bookon.feature.ranking.domain

import javax.inject.Inject

class GetReaderRankingUseCase @Inject constructor(private val repository: RankingRepository) {
    /** 랭킹 화면 진입 시 선택 연도의 다독 학생 목록을 조회한다. */
    suspend operator fun invoke(year: Int, limit: Int) = repository.readers(year, limit)
}
