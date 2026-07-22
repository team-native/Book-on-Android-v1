package com.teamnative.bookon.feature.marathon.domain

import javax.inject.Inject

class GetRead365MyInfoUseCase @Inject constructor(private val repository: MarathonRepository) {
    /** 마이페이지 진입 시 read365 세션 연동 여부를 확인한다. */
    suspend operator fun invoke() = repository.read365MyInfo()
}
