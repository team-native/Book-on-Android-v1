package com.teamnative.bookon.feature.my.presentation.main

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.fcm.domain.ClearFcmTokenOnLogoutUseCase
import com.teamnative.bookon.feature.fcm.domain.FcmRepository
import com.teamnative.bookon.feature.fcm.domain.FcmTokenRegistration
import com.teamnative.bookon.feature.fcm.domain.FcmTokenUnregistration
import com.teamnative.bookon.feature.fcm.domain.UnregisterFcmTokenUseCase
import com.teamnative.bookon.core.notification.FcmTokenProvider
import com.teamnative.bookon.feature.marathon.domain.GetRead365MyInfoUseCase
import com.teamnative.bookon.feature.marathon.domain.MarathonRepository
import com.teamnative.bookon.feature.marathon.domain.Read365MyInfo
import com.teamnative.bookon.feature.my.domain.AccountDeletion
import com.teamnative.bookon.feature.my.domain.FavoriteBookPage
import com.teamnative.bookon.feature.my.domain.GetMyProfileUseCase
import com.teamnative.bookon.feature.my.domain.MyLoan
import com.teamnative.bookon.feature.my.domain.MyLoanPage
import com.teamnative.bookon.feature.my.domain.MyProfile
import com.teamnative.bookon.feature.my.domain.MyRepository
import com.teamnative.bookon.feature.my.domain.MyUser
import com.teamnative.bookon.feature.my.domain.NotificationSettings
import com.teamnative.bookon.feature.my.domain.ProfileImage
import com.teamnative.bookon.feature.my.domain.RequestAccountDeletionUseCase
import com.teamnative.bookon.feature.my.domain.UpdateNotificationSettingsUseCase
import com.teamnative.bookon.feature.my.domain.UploadProfileImageUseCase
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnMyViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `프로필 조회 성공 시 서버 프로필 이미지 URL을 화면 상태에 반영한다`() = runTest {
        val repository = MyRepositoryFake(
            profileResult = NetworkResult.Success(
                profile().copy(profileImageUrl = "https://example.com/profile.jpg"),
            ),
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            "https://example.com/profile.jpg",
            viewModel.uiState.value.profileImageUrl,
        )
        assertEquals("홍길동", viewModel.uiState.value.userNameText)
    }

    @Test
    fun `Read365 연동 성공 시 샘플 진행률 없이 연동 상태만 반영한다`() = runTest {
        val viewModel = createViewModel(
            repository = MyRepositoryFake(),
            marathonRepository = MarathonRepositoryFake(),
        )

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isReadingMarathonLinked)
        assertEquals("", viewModel.uiState.value.marathon.progressText)
        assertEquals(0f, viewModel.uiState.value.marathon.progress)
    }

    @Test
    fun `재시도 시 프로필과 Read365 연동 상태를 함께 다시 조회한다`() = runTest {
        val profileCallCount = AtomicInteger(0)
        val read365CallCount = AtomicInteger(0)
        val viewModel = createViewModel(
            repository = MyRepositoryFake(
                onProfile = { profileCallCount.incrementAndGet() },
            ),
            marathonRepository = MarathonRepositoryFake(
                onRead365MyInfo = { read365CallCount.incrementAndGet() },
            ),
        )

        advanceUntilIdle()
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(2, profileCallCount.get())
        assertEquals(2, read365CallCount.get())
    }

    @Test
    fun `프로필 이미지 업로드 성공 시 반환 URL과 업로드 완료 상태를 반영한다`() = runTest {
        val uploadedBytes = AtomicReference<ByteArray>()
        val repository = MyRepositoryFake(
            uploadProfileImageResult = NetworkResult.Success(
                ProfileImage("https://example.com/new-profile.jpg"),
            ),
            onUploadProfileImage = { _, imageBytes -> uploadedBytes.set(imageBytes) },
        )
        val viewModel = createViewModel(repository)
        val imageBytes = byteArrayOf(1, 2, 3)

        advanceUntilIdle()
        viewModel.uploadProfileImage("image/jpeg", imageBytes)
        advanceUntilIdle()

        assertEquals(
            "https://example.com/new-profile.jpg",
            viewModel.uiState.value.profileImageUrl,
        )
        assertFalse(viewModel.uiState.value.isProfileImageUploading)
        assertArrayEquals(imageBytes, uploadedBytes.get())
    }

    @Test
    fun `프로필 이미지 업로드 실패 시 오류 상태를 표시하고 업로드 상태를 종료한다`() = runTest {
        val repository = MyRepositoryFake(
            uploadProfileImageResult = NetworkResult.Failure(
                NetworkError.Network(IllegalStateException("network")),
            ),
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()
        viewModel.uploadProfileImage("image/jpeg", byteArrayOf(1))
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isProfileImageUploading)
        assertTrue(viewModel.uiState.value.profileImageErrorMessage is BookOnUiMessage.Resource)
    }

    @Test
    fun `회원 탈퇴 요청 성공 시 FCM 토큰을 해제하고 onSuccess를 호출한다`() = runTest {
        val fcmRepository = RecordingFcmRepository()
        val onSuccessCallCount = AtomicInteger(0)
        val viewModel = createViewModel(
            repository = MyRepositoryFake(
                requestAccountDeletionResult = NetworkResult.Success(
                    AccountDeletion(requestId = 1L, status = "PENDING", requestedAt = "2026-09-07T00:00:00Z"),
                ),
            ),
            fcmRepository = fcmRepository,
        )

        advanceUntilIdle()
        viewModel.requestAccountDeletion(reason = "테스트 사유") { onSuccessCallCount.incrementAndGet() }
        advanceUntilIdle()

        assertTrue(fcmRepository.unregisterInvoked)
        assertEquals(1, onSuccessCallCount.get())
        assertFalse(viewModel.uiState.value.isAccountDeletionInProgress)
    }

    @Test
    fun `회원 탈퇴 요청 실패 시 오류 상태를 표시하고 onSuccess를 호출하지 않는다`() = runTest {
        val onSuccessCallCount = AtomicInteger(0)
        val viewModel = createViewModel(
            repository = MyRepositoryFake(
                requestAccountDeletionResult = NetworkResult.Failure(
                    NetworkError.Http(404, 4040, "요청하신 API를 찾을 수 없습니다."),
                ),
            ),
        )

        advanceUntilIdle()
        viewModel.requestAccountDeletion { onSuccessCallCount.incrementAndGet() }
        advanceUntilIdle()

        assertEquals(0, onSuccessCallCount.get())
        assertFalse(viewModel.uiState.value.isAccountDeletionInProgress)
        assertTrue(viewModel.uiState.value.accountDeletionErrorMessage is BookOnUiMessage.Dynamic)
    }

    @Test
    fun `회원 탈퇴 요청 실패 시 FCM 토큰을 해제하지 않는다`() = runTest {
        val fcmRepository = RecordingFcmRepository()
        val viewModel = createViewModel(
            repository = MyRepositoryFake(
                requestAccountDeletionResult = NetworkResult.Failure(
                    NetworkError.Network(IllegalStateException("network")),
                ),
            ),
            fcmRepository = fcmRepository,
        )

        advanceUntilIdle()
        viewModel.requestAccountDeletion { }
        advanceUntilIdle()

        assertFalse(fcmRepository.unregisterInvoked)
    }

    private fun createViewModel(
        repository: MyRepositoryFake,
        marathonRepository: MarathonRepository = MarathonRepositoryFake(),
        fcmRepository: FcmRepository = RecordingFcmRepository(),
        fcmTokenProvider: FcmTokenProvider = FakeFcmTokenProvider(),
    ): BookOnMyViewModel {
        return BookOnMyViewModel(
            getMyProfile = GetMyProfileUseCase(repository),
            updateNotificationSettings = UpdateNotificationSettingsUseCase(repository),
            getRead365MyInfo = GetRead365MyInfoUseCase(marathonRepository),
            uploadProfileImageUseCase = UploadProfileImageUseCase(repository),
            requestAccountDeletionUseCase = RequestAccountDeletionUseCase(repository),
            clearFcmTokenOnLogoutUseCase = ClearFcmTokenOnLogoutUseCase(
                fcmTokenProvider,
                UnregisterFcmTokenUseCase(fcmRepository),
            ),
        )
    }
}

private class FakeFcmTokenProvider(private val token: String? = "fake-fcm-token") : FcmTokenProvider {
    override suspend fun currentToken(): String? = token
}

private class RecordingFcmRepository : FcmRepository {
    var unregisterInvoked = false
        private set

    override suspend fun registerToken(token: String, platform: String): NetworkResult<FcmTokenRegistration> {
        error("not used")
    }

    override suspend fun unregisterToken(token: String): NetworkResult<FcmTokenUnregistration> {
        unregisterInvoked = true
        return NetworkResult.Success(FcmTokenUnregistration(unregistered = true))
    }
}

private fun profile() = MyProfile(
    name = "홍길동",
    department = "AI과",
    profileImageUrl = null,
    currentLoanCount = 3,
    overdueCount = 2,
    totalLoanCount = 23,
    notificationSettings = NotificationSettings(
        dueDateReminder = false,
        newBookReminder = false,
    ),
)

private class MarathonRepositoryFake(
    private val onRead365MyInfo: () -> Unit = {},
) : MarathonRepository {
    override suspend fun read365MyInfo(): NetworkResult<Read365MyInfo> {
        onRead365MyInfo()
        return NetworkResult.Success(
            Read365MyInfo(read365Id = "read365-id"),
        )
    }
}

private class MyRepositoryFake(
    private val profileResult: NetworkResult<MyProfile> = NetworkResult.Success(profile()),
    private val uploadProfileImageResult: NetworkResult<ProfileImage> = NetworkResult.Success(
        ProfileImage("https://example.com/profile.jpg"),
    ),
    private val onUploadProfileImage: (String, ByteArray) -> Unit = { _, _ -> },
    private val onProfile: () -> Unit = {},
    private val requestAccountDeletionResult: NetworkResult<AccountDeletion> = NetworkResult.Success(
        AccountDeletion(requestId = 1L, status = "PENDING", requestedAt = "2026-09-07T00:00:00Z"),
    ),
) : MyRepository {
    override suspend fun profile(): NetworkResult<MyProfile> {
        onProfile()
        return profileResult
    }

    override suspend fun updateNotificationSettings(
        dueDateReminder: Boolean,
        newBookReminder: Boolean,
    ): NetworkResult<NotificationSettings> {
        return NetworkResult.Success(NotificationSettings(dueDateReminder, newBookReminder))
    }

    override suspend fun updateProfile(
        name: String?,
        department: String?,
        grade: Int?,
        classNo: Int?,
        studentNo: String?,
    ): NetworkResult<MyUser> {
        error("not used")
    }

    override suspend fun requestAccountDeletion(reason: String?): NetworkResult<AccountDeletion> {
        return requestAccountDeletionResult
    }

    override suspend fun uploadProfileImage(
        contentType: String,
        imageBytes: ByteArray,
    ): NetworkResult<ProfileImage> {
        onUploadProfileImage(contentType, imageBytes)
        return uploadProfileImageResult
    }

    override suspend fun deleteProfileImage(): NetworkResult<ProfileImage> {
        error("not used")
    }

    override suspend fun currentLoans(): NetworkResult<List<MyLoan>> {
        error("not used")
    }

    override suspend fun loanHistory(
        page: Int,
        size: Int,
        status: String,
    ): NetworkResult<MyLoanPage> {
        error("not used")
    }

    override suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPage> {
        error("not used")
    }
}
