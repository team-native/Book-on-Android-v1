package com.teamnative.bookon.feature.fcm.domain

import com.teamnative.bookon.core.notification.FcmTokenProvider
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class RegisterFcmTokenUseCase @Inject constructor(
    private val repository: FcmRepository,
) {
    /** onNewToken 콜백 등에서 이미 확보한 토큰 값을 서버에 등록한다. */
    suspend operator fun invoke(token: String) = repository.registerToken(token)
}

class UnregisterFcmTokenUseCase @Inject constructor(
    private val repository: FcmRepository,
) {
    /** 이미 확보한 토큰 값을 서버에서 해제한다. */
    suspend operator fun invoke(token: String) = repository.unregisterToken(token)
}

class SyncFcmTokenOnAuthenticationUseCase @Inject constructor(
    private val fcmTokenProvider: FcmTokenProvider,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
) {
    /**
     * 로그인 성공 또는 자동 로그인 성공 직후 현재 기기의 FCM 토큰을 조회해 서버에 등록한다.
     * 로그인·세션 복원 흐름을 막지 않는 최선형(best-effort) 작업이므로 취소를 제외한 모든 실패를 흡수한다.
     */
    suspend operator fun invoke() {
        try {
            val token = fcmTokenProvider.currentToken() ?: return
            registerFcmTokenUseCase(token)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            // 등록 실패는 로그인 흐름에 영향을 주지 않는다. 다음 로그인 또는 세션 복원 시 다시 시도된다.
        }
    }
}

class ClearFcmTokenOnLogoutUseCase @Inject constructor(
    private val fcmTokenProvider: FcmTokenProvider,
    private val unregisterFcmTokenUseCase: UnregisterFcmTokenUseCase,
) {
    /**
     * 로그아웃 직전(Authorization이 아직 유효할 때) 현재 기기의 FCM 토큰을 서버에서 해제한다.
     * 로그아웃 흐름을 막지 않는 최선형 작업이므로 취소를 제외한 모든 실패를 흡수한다.
     */
    suspend operator fun invoke() {
        try {
            val token = fcmTokenProvider.currentToken() ?: return
            unregisterFcmTokenUseCase(token)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            // 해제 실패는 로그아웃 흐름에 영향을 주지 않는다.
        }
    }
}
