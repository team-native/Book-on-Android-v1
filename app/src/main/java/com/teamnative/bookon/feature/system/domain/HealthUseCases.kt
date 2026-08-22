package com.teamnative.bookon.feature.system.domain

import javax.inject.Inject

class CheckHealthUseCase @Inject constructor(
    private val repository: HealthRepository,
) {
    /** 앱의 서버 연결 상태를 확인한다. */
    suspend operator fun invoke() = repository.health()
}
