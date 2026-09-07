package com.teamnative.bookon.core.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.network.auth.TokenSessionManager
import com.teamnative.bookon.feature.fcm.domain.RegisterFcmTokenUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class FcmTokenRegistrationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val tokenSessionManager: TokenSessionManager,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
) : CoroutineWorker(context, params) {

    /** onNewToken에서 전달된 토큰을, 로그인 상태일 때만 신뢰성 있게 서버에 등록한다. 미로그인 상태면 성공으로 스킵한다. */
    override suspend fun doWork(): Result {
        val token = inputData.getString(TokenKey) ?: return Result.failure()
        if (tokenSessionManager.accessToken() == null) {
            return Result.success()
        }

        return when (val result = registerFcmTokenUseCase(token)) {
            is NetworkResult.Success -> Result.success()
            is NetworkResult.Failure -> when (val error = result.error) {
                is NetworkError.Network -> Result.retry()
                is NetworkError.Http -> if (error.statusCode >= 500) Result.retry() else Result.failure()
                else -> Result.retry()
            }
        }
    }

    companion object {
        private const val TokenKey = "fcm_token"
        private const val UniqueWorkName = "fcm-token-registration"

        /** FirebaseMessagingService가 네트워크를 직접 호출하지 않고 신뢰성 있는 등록을 예약하도록 돕는다. */
        fun enqueue(context: Context, token: String) {
            val request = OneTimeWorkRequestBuilder<FcmTokenRegistrationWorker>()
                .setInputData(workDataOf(TokenKey to token))
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, WorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(UniqueWorkName, ExistingWorkPolicy.REPLACE, request)
        }
    }
}
