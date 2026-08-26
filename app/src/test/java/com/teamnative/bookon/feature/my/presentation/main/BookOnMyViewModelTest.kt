package com.teamnative.bookon.feature.my.presentation.main

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
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
import com.teamnative.bookon.feature.my.domain.UpdateNotificationSettingsUseCase
import com.teamnative.bookon.feature.my.domain.UploadProfileImageUseCase
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

    private fun createViewModel(repository: MyRepositoryFake): BookOnMyViewModel {
        return BookOnMyViewModel(
            getMyProfile = GetMyProfileUseCase(repository),
            updateNotificationSettings = UpdateNotificationSettingsUseCase(repository),
            getRead365MyInfo = GetRead365MyInfoUseCase(MarathonRepositoryFake()),
            uploadProfileImageUseCase = UploadProfileImageUseCase(repository),
        )
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

private class MarathonRepositoryFake : MarathonRepository {
    override suspend fun read365MyInfo(): NetworkResult<Read365MyInfo> {
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
) : MyRepository {
    override suspend fun profile(): NetworkResult<MyProfile> {
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
        error("not used")
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
