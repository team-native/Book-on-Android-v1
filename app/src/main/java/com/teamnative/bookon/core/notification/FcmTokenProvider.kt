package com.teamnative.bookon.core.notification

import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

interface FcmTokenProvider {
    suspend fun currentToken(): String?
}

/** Firebase가 관리하는 현재 기기의 FCM 등록 토큰을 비동기로 조회하고, 실패 시 null을 반환한다. */
@Singleton
class FcmTokenProviderImpl @Inject constructor() : FcmTokenProvider {
    override suspend fun currentToken(): String? = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token -> continuation.resume(token) }
            .addOnFailureListener { continuation.resume(null) }
    }
}
