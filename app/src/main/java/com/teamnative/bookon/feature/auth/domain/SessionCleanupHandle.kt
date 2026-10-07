package com.teamnative.bookon.feature.auth.domain

/** 종료된 세션 정리에만 사용할 수 있는 일회용 핸들이다. */
interface SessionCleanupHandle {
    fun dispose()
}
