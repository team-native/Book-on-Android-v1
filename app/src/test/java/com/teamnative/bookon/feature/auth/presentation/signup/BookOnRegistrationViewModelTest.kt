package com.teamnative.bookon.feature.auth.presentation.signup

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.AuthRepository
import com.teamnative.bookon.feature.auth.domain.LinkRead365UseCase
import com.teamnative.bookon.feature.auth.domain.LoginSession
import com.teamnative.bookon.feature.auth.domain.LoginUseCase
import com.teamnative.bookon.feature.auth.domain.PasswordResetEmailSession
import com.teamnative.bookon.feature.auth.domain.RegistrationDraft
import com.teamnative.bookon.feature.auth.domain.RegistrationSession
import com.teamnative.bookon.feature.auth.domain.RegisterUseCase
import com.teamnative.bookon.feature.auth.domain.RegisteredUser
import com.teamnative.bookon.feature.auth.domain.ResetPasswordUseCase
import com.teamnative.bookon.feature.auth.domain.SendPasswordResetEmailUseCase
import com.teamnative.bookon.feature.auth.domain.VerifyRegistrationUseCase
import com.teamnative.bookon.feature.auth.domain.LogoutUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
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
                privacyAccepted = true,
                password = "Password1!",
                passwordConfirm = "Password1!",
            )
        }

        viewModel.resendVerification()

        assertEquals(
            1,
            repository.registerRequests
        )
        assertEquals(
            "renewed-session",
            viewModel.state.value.sessionId
        )
        assertEquals(
            null,
            viewModel.state.value.errorMessage
        )
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
                privacyAccepted = true,
                password = "Password1!",
                passwordConfirm = "Password1!",
            )
        }
        viewModel.requestVerification(
            onSuccess = {
            },
            onEmailAlreadyUsed = {
                returnedToEmailStep = true
            },
        )

        assertTrue(returnedToEmailStep)
        assertEquals(
            BookOnRegistrationEmailError.AlreadyUsed,
            viewModel.state.value.emailError
        )
        assertEquals(
            null,
            viewModel.state.value.errorMessage
        )
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `이메일 입력값이 변경되면 기존 이메일 오류를 지운다`() = runTest {
        val viewModel = createViewModel(RegistrationRepository())
        viewModel.update {
            it.copy(emailError = BookOnRegistrationEmailError.AlreadyUsed)
        }

        viewModel.update { state ->
            state.copy(email = "new-email")
        }

        assertEquals(
            null,
            viewModel.state.value.emailError
        )
    }

    @Test
    fun `final empty fields show an error without sending registration`() = runTest {
        val repository = RegistrationRepository()
        val viewModel = createViewModel(repository)
        viewModel.requestVerification(
            {
            },
            {
            }
        )
        assertTrue(viewModel.state.value.hasMissingRegistrationFields)
        assertEquals(
            0,
            repository.registerRequests
        )
    }

    @Test
    fun `server registration failure preserves inputs and ends loading`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = NetworkResult.Failure(NetworkError.Http(
                    422,
                    4220,
                    "invalid fields"
            ))
        }
        val viewModel = createViewModel(repository)
        validInput(viewModel)
        viewModel.requestVerification(
            {
            },
            {
            }
        )
        assertEquals(
            "invalid fields",
            viewModel.state.value.errorMessage
        )
        assertEquals(
            "student",
            viewModel.state.value.name
        )
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `malformed deadline never navigates with a fabricated timer`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = NetworkResult.Success(RegistrationSession(
                    "session",
                    "bad",
                    "server@school.kr"
            ))
        }
        val viewModel = createViewModel(repository)
        validInput(viewModel)
        var navigated = false
        viewModel.requestVerification(
            {
                navigated = true
            },
            {
            }
        )
        assertFalse(navigated)
        assertTrue(viewModel.state.value.hasInvalidDeadline)
        assertEquals(
            null,
            viewModel.state.value.sessionId
        )
        assertEquals(
            0L,
            viewModel.state.value.remainingSeconds
        )
    }

    @Test
    fun `deadline recomputes after foreground return and expired code cannot submit`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = validSession()
        }
        val clock = RegistrationClock()
        val viewModel = BookOnRegistrationViewModel(
            RegisterUseCase(repository),
            VerifyRegistrationUseCase(repository),
            clock
        )
        validInput(viewModel)
        viewModel.requestVerification(
            {
            },
            {
            }
        )
        viewModel.updateCode("123456")
        assertEquals(
            "server@school.kr",
            viewModel.state.value.verificationEmail
        )
        assertEquals(
            5L,
            viewModel.state.value.remainingSeconds
        )
        clock.current = clock.current.plusSeconds(6)
        viewModel.updateRemainingTime()
        viewModel.verify(
            "123456",
            {
            }
        )
        assertEquals(
            0L,
            viewModel.state.value.remainingSeconds
        )
        assertEquals(
            0,
            repository.verifyRequests
        )
        clear(viewModel)
    }

    @Test
    fun `resending blocks verification and replaces session and clears code only on success`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = validSession()
        }
        val viewModel = BookOnRegistrationViewModel(
            RegisterUseCase(repository),
            VerifyRegistrationUseCase(repository),
            RegistrationClock()
        )
        validInput(viewModel)
        viewModel.requestVerification(
            {
            },
            {
            }
        )
        viewModel.updateCode("123456")
        val pending = CompletableDeferred<NetworkResult<RegistrationSession>>()
        repository.registerGate = pending
        viewModel.resendVerification()
        viewModel.resendVerification()
        viewModel.verify(
            "123456",
            {
            }
        )
        assertEquals(
            2,
            repository.registerRequests
        )
        assertEquals(
            0,
            repository.verifyRequests
        )
        pending.complete(NetworkResult.Success(RegistrationSession(
                    "replaced",
                    "2026-10-04T00:00:10Z",
                    "server@school.kr"
        )))
        assertEquals(
            "replaced",
            viewModel.state.value.sessionId
        )
        assertEquals(
            "",
            viewModel.state.value.verificationCode
        )
        assertEquals(
            10L,
            viewModel.state.value.remainingSeconds
        )
        clear(viewModel)
    }

    @Test
    fun `failed resend retains the valid session and input code`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = validSession()
        }
        val viewModel = BookOnRegistrationViewModel(
            RegisterUseCase(repository),
            VerifyRegistrationUseCase(repository),
            RegistrationClock()
        )
        validInput(viewModel)
        viewModel.requestVerification(
            {
            },
            {
            }
        )
        viewModel.updateCode("123456")
        repository.registerResult = NetworkResult.Failure(NetworkError.Network(java.io.IOException("offline")))
        viewModel.resendVerification()
        assertEquals(
            "session",
            viewModel.state.value.sessionId
        )
        assertEquals(
            "123456",
            viewModel.state.value.verificationCode
        )
        assertEquals(
            5L,
            viewModel.state.value.remainingSeconds
        )
        assertFalse(viewModel.state.value.isLoading)
        clear(viewModel)
    }

    @Test
    fun `verification blocks duplicate confirmation and resend`() = runTest {
        val repository = RegistrationRepository().apply {
            registerResult = validSession()
        }
        val viewModel = BookOnRegistrationViewModel(
            RegisterUseCase(repository),
            VerifyRegistrationUseCase(repository),
            RegistrationClock()
        )
        validInput(viewModel)
        viewModel.requestVerification(
            {
            },
            {
            }
        )
        viewModel.updateCode("123456")
        viewModel.verify(
            "12",
            {
            }
        )
        assertEquals(
            0,
            repository.verifyRequests
        )
        val pending = CompletableDeferred<NetworkResult<RegisteredUser>>()
        repository.verifyGate = pending
        viewModel.verify(
            "123456",
            {
            }
        )
        viewModel.verify(
            "123456",
            {
            }
        )
        viewModel.resendVerification()
        assertEquals(
            1,
            repository.verifyRequests
        )
        assertEquals(
            1,
            repository.registerRequests
        )
        pending.complete(NetworkResult.Failure(NetworkError.Http(
                    401,
                    4012,
                    "wrong code"
        )))
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(
            "123456",
            viewModel.state.value.verificationCode
        )
        clear(viewModel)
    }

    @Test
    fun `back cancels pending registration and late success never navigates`() = runTest {
        val pending = CompletableDeferred<NetworkResult<RegistrationSession>>()
        val repository = RegistrationRepository().apply {
            registerGate = pending
            ignoreCancellation = true
        }
        val viewModel = createViewModel(repository)
        validInput(viewModel)
        var navigated = false
        viewModel.requestVerification(
            {
                navigated = true
            },
            {
            }
        )
        viewModel.cancelPendingRequest()
        pending.complete(validSession())
        assertFalse(navigated)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(
            null,
            viewModel.state.value.sessionId
        )
    }

    private fun validInput(viewModel: BookOnRegistrationViewModel) {
        viewModel.update {
            it.copy(
                email = "student",
                name = "student",
                department = BookOnDepartment.AI,
                gender = BookOnGender.FEMALE,
                password = "Password1!",
                passwordConfirm = "Password1!",
                privacyAccepted = true
            )
        }
    }

    private fun validSession() = NetworkResult.Success(RegistrationSession(
            "session",
            "2026-10-04T00:00:05Z",
            "server@school.kr"
    ))

    private fun clear(viewModel: BookOnRegistrationViewModel) {
        androidx.lifecycle.ViewModelStore().apply {
            put(
                "registration",
                viewModel
            )
            clear()
        }
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
    var verifyRequests = 0
    var ignoreCancellation = false
    var registerGate: CompletableDeferred<NetworkResult<RegistrationSession>>? = null
    var verifyGate: CompletableDeferred<NetworkResult<RegisteredUser>>? = null
    var registerResult: NetworkResult<RegistrationSession>? = null

    override suspend fun register(request: RegistrationDraft): NetworkResult<RegistrationSession> {
        registerRequests += 1
        registerGate?.let { pending ->

            return if (ignoreCancellation) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) {
                    pending.await()
                }
            } else {
                pending.await()
            }
        }
        return registerResult ?: NetworkResult.Success(
            RegistrationSession(
                "renewed-session",
                "2026-07-22T00:00:00Z",
                request.email,
            ),
        )
    }

    override suspend fun verifyRegistration(
        sessionId: String,
        passcode: String
    ): NetworkResult<RegisteredUser> {
        verifyRequests++
        return verifyGate?.await() ?: error("not used")
    }
    override suspend fun login(
        loginId: String,
        password: String
    ): NetworkResult<LoginSession> = error("not used")
    override suspend fun logout(refreshToken: String): NetworkResult<Unit> = error("not used")
    override suspend fun sendPasswordResetEmail(email: String): NetworkResult<PasswordResetEmailSession> = error("not used")
    override suspend fun resetPassword(
        email: String,
        code: String,
        password: String,
        passwordConfirm: String
    ): NetworkResult<Unit> = error("not used")
    override suspend fun linkRead365(
        id: String,
        password: String
    ): NetworkResult<Unit> = error("not used")
}

private class RegistrationClock : Clock() {
    var current: Instant = Instant.parse("2026-10-04T00:00:00Z")
    override fun getZone(): ZoneId = ZoneId.of("UTC")
    override fun withZone(zone: ZoneId): Clock = this
    override fun instant(): Instant = current
}
