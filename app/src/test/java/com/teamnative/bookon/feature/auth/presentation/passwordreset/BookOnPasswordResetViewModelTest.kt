package com.teamnative.bookon.feature.auth.presentation.passwordreset

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.AuthRepository
import com.teamnative.bookon.feature.auth.domain.LoginSession
import com.teamnative.bookon.feature.auth.domain.RegistrationDraft
import com.teamnative.bookon.feature.auth.domain.RegistrationSession
import com.teamnative.bookon.feature.auth.domain.RegisteredUser
import com.teamnative.bookon.feature.auth.domain.ResetPasswordUseCase
import com.teamnative.bookon.feature.auth.domain.SendPasswordResetEmailUseCase
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
class BookOnPasswordResetViewModelTest {
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
    fun `인증 메일 발송에 성공하면 다음 화면 이동을 요청한다`() = runTest {
        val repository = PasswordResetRepository()
        val viewModel = createViewModel(repository)
        var navigated = false

        viewModel.updateEmail("s26031")
        viewModel.sendVerificationCode { navigated = true }

        assertEquals("s26031", repository.lastEmail)
        assertTrue(navigated)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(null, viewModel.state.value.error)
    }

    @Test
    fun `인증 메일 재전송은 입력 코드를 비우고 실패는 오류 상태로 표시한다`() = runTest {
        val repository = PasswordResetRepository()
        val viewModel = createViewModel(repository)

        viewModel.updateEmail("s26031")
        viewModel.updateVerificationCode("123456")
        viewModel.resendVerificationCode()

        assertEquals("", viewModel.state.value.code)
        assertEquals(1, repository.sendEmailRequestCount)

        repository.sendEmailResult = NetworkResult.Failure(
            NetworkError.Network(IllegalStateException("network")),
        )
        viewModel.resendVerificationCode()

        assertEquals(PasswordResetError.RequestFailed, viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `비밀번호 재설정에 성공하면 로그인 복귀 콜백을 호출한다`() = runTest {
        val repository = PasswordResetRepository()
        val viewModel = createViewModel(repository)
        var completed = false

        viewModel.updateEmail("s26031")
        viewModel.updateVerificationCode("123456")
        viewModel.updatePassword("Password1!")
        viewModel.updatePasswordConfirm("Password1!")
        viewModel.resetPassword { completed = true }

        assertEquals("s26031", repository.resetEmail)
        assertEquals("123456", repository.resetCode)
        assertEquals("Password1!", repository.resetPassword)
        assertTrue(completed)
        assertFalse(viewModel.state.value.isLoading)
    }

    private fun createViewModel(repository: PasswordResetRepository): BookOnPasswordResetViewModel {
        return BookOnPasswordResetViewModel(
            sendEmail = SendPasswordResetEmailUseCase(repository),
            resetPassword = ResetPasswordUseCase(repository),
        )
    }
}

private class PasswordResetRepository : AuthRepository {
    var sendEmailRequestCount = 0
    var sendEmailResult: NetworkResult<Unit> = NetworkResult.Success(Unit)
    var lastEmail = ""
    var resetEmail = ""
    var resetCode = ""
    var resetPassword = ""

    override suspend fun sendPasswordResetEmail(email: String): NetworkResult<Unit> {
        sendEmailRequestCount += 1
        lastEmail = email
        return sendEmailResult
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        password: String,
        passwordConfirm: String,
    ): NetworkResult<Unit> {
        resetEmail = email
        resetCode = code
        resetPassword = password
        return NetworkResult.Success(Unit)
    }

    override suspend fun register(request: RegistrationDraft): NetworkResult<RegistrationSession> {
        error("not used")
    }

    override suspend fun verifyRegistration(
        sessionId: String,
        passcode: String,
    ): NetworkResult<RegisteredUser> {
        error("not used")
    }

    override suspend fun login(loginId: String, password: String): NetworkResult<LoginSession> {
        error("not used")
    }

    override suspend fun logout(refreshToken: String): NetworkResult<Unit> {
        error("not used")
    }

    override suspend fun linkRead365(id: String, password: String): NetworkResult<Unit> {
        error("not used")
    }
}
