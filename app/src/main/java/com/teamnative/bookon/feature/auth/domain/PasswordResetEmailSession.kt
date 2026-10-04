package com.teamnative.bookon.feature.auth.domain

/** 비밀번호 재설정 인증 메일 발송 결과와 인증코드의 유효 시간을 표현한다. */
data class PasswordResetEmailSession(
    val email: String,
    val expiresInSeconds: Long,
)
