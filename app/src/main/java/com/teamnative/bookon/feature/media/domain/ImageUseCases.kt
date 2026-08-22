package com.teamnative.bookon.feature.media.domain

import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val repository: ImageRepository,
) {
    /** 서버 파일 경로를 이미지 바이트로 조회한다. */
    suspend operator fun invoke(file: String) = repository.image(file)
}
