package com.teamnative.bookon.core.network.auth

import java.io.IOException

/** 저장 실패를 토큰이나 원본 저장 내용을 노출하지 않는 오류로 전달한다. */
class SessionStorageException(cause: Throwable) : IOException("Session storage unavailable", cause)
