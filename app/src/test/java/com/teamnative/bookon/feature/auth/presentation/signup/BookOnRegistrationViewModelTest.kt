package com.teamnative.bookon.feature.auth.presentation.signup

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.AuthRepository
import com.teamnative.bookon.feature.auth.domain.LinkRead365UseCase
import com.teamnative.bookon.feature.auth.domain.LoginSession
import com.teamnative.bookon.feature.auth.domain.LoginUseCase
import com.teamnative.bookon.feature.auth.domain.RegistrationDraft
import com.teamnative.bookon.feature.auth.domain.RegistrationSession
import com.teamnative.bookon.feature.auth.domain.RegisterUseCase
import com.teamnative.bookon.feature.auth.domain.RegisteredUser
import com.teamnative.bookon.feature.auth.domain.ResetPasswordUseCase
import com.teamnative.bookon.feature.auth.domain.SendPasswordResetEmailUseCase
import com.teamnative.bookon.feature.auth.domain.VerifyRegistrationUseCase
import com.teamnative.bookon.feature.auth.domain.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnRegistrationViewModelTest {
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
    fun `인증번호 재전송은 새 서버 세션을 화면 상태에 반영한다`() = runTest {
        val repository = RegistrationRepository()
        val viewModel = BookOnRegistrationViewModel(
            registerUseCase = RegisterUseCase(repository),
            verifyRegistrationUseCase = VerifyRegistrationUseCase(repository),
        )
        viewModel.update {
            it.copy(
                email = "student@school.kr",
                name = "학생",
                department = BookOnDepartment.AI,
                gender = BookOnGender.FEMALE,
                password = "Password1!",
                passwordConfirm = "Password1!",
            )
        }

        viewModel.resendVerification()

        assertEquals(1, repository.registerRequests)
        assertEquals("renewed-session", viewModel.state.value.sessionId)
        assertEquals(null, viewModel.state.value.errorMessage)
    }

    @Test
    fun `이미 사용 중인 이메일이면 이메일 단계 복귀와 오류 표시를 요청한다`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = NetworkResult.Failure(
                NetworkError.Http(
                    statusCode = 409,
                    errorCode = null,
                    message = "이미 사용 중인 이메일입니다.",
                ),
            )
        }
        val viewModel = createViewModel(repository)
        var returnedToEmailStep = false

        viewModel.update {
            it.copy(
                email = "s26031",
                name = "학생",
                department = BookOnDepartment.AI,
                gender = BookOnGender.FEMALE,
                password = "Password1!",
                passwordConfirm = "Password1!",
            )
        }
        viewModel.requestVerification(
            onSuccess = {},
            onEmailAlreadyUsed = { returnedToEmailStep = true },
        )

        assertTrue(returnedToEmailStep)
        assertEquals(BookOnRegistrationEmailError.AlreadyUsed, viewModel.state.value.emailError)
        assertEquals(null, viewModel.state.value.errorMessage)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `이메일 입력값이 변경되면 기존 이메일 오류를 지운다`() = runTest {
        val viewModel = createViewModel(RegistrationRepository())
        viewModel.update {
            it.copy(emailError = BookOnRegistrationEmailError.AlreadyUsed)
        }

        viewModel.update { state -> state.copy(email = "new-email") }

        assertEquals(null, viewModel.state.value.emailError)
    }

    private fun createViewModel(repository: RegistrationRepository): BookOnRegistrationViewModel {
        return BookOnRegistrationViewModel(
            registerUseCase = RegisterUseCase(repository),
            verifyRegistrationUseCase = VerifyRegistrationUseCase(repository),
        )
    }
}

private class RegistrationRepository : AuthRepository {
    var registerRequests = 0
    var registerResult: NetworkResult<RegistrationSession>? = null

    override suspend fun register(request: RegistrationDraft): NetworkResult<RegistrationSession> {
        registerRequests += 1
        return registerResult ?: NetworkResult.Success(
            RegistrationSession(
                "renewed-session",
                "2026-07-22T00:00:00Z",
                request.email,
            ),
        )
    }

    override suspend fun verifyRegistration(sessionId: String, passcode: String): NetworkResult<RegisteredUser> = error("not used")
    override suspend fun login(loginId: String, password: String): NetworkResult<LoginSession> = error("not used")
    override suspend fun logout(refreshToken: String): NetworkResult<Unit> = error("not used")
    override suspend fun sendPasswordResetEmail(email: String): NetworkResult<Unit> = error("not used")
    override suspend fun resetPassword(email: String, code: String, password: String, passwordConfirm: String): NetworkResult<Unit> = error("not used")
    override suspend fun linkRead365(id: String, password: String): NetworkResult<Unit> = error("not used")
}
