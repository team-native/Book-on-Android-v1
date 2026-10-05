package com.teamnative.bookon.feature.auth.presentation.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.ResetPasswordUseCase
import com.teamnative.bookon.feature.auth.domain.SendPasswordResetEmailUseCase
import com.teamnative.bookon.feature.auth.presentation.component.BookOnPasswordPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Clock
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val GSM_EMAIL_DOMAIN = "@gsm.hs.kr"

@HiltViewModel
class BookOnPasswordResetViewModel @Inject constructor(
    private val sendEmail: SendPasswordResetEmailUseCase,
    private val resetPassword: ResetPasswordUseCase,
    private val clock: Clock = Clock.systemUTC(),
) : ViewModel() {
    private val _state = MutableStateFlow(PasswordResetFormState())
    val state = _state.asStateFlow()
    private val navigationChannel = Channel<PasswordResetNavigation>(Channel.BUFFERED)
    val navigationEvents = navigationChannel.receiveAsFlow()
    private var countdownJob: Job? = null
    private var requestJob: Job? = null
    private var generation = 0L
    private var deadline: Long? = null
    val hasVerificationSession: Boolean get() = deadline != null

    fun updateEmail(email: String) {
        if (_state.value.isLoading) {
            return
        }
        if (_state.value.email != email) {
            deadline = null
            countdownJob?.cancel()
            _state.update { it.copy(code = "", verificationRemainingSeconds = 0) }
        }
        _state.update { it.copy(email = email, error = null) }
    }

    fun updateVerificationCode(code: String) {
        if (!_state.value.isLoading) {
            _state.update { it.copy(code = code.filter(Char::isDigit).take(6), error = null) }
        }
    }

    fun updatePassword(password: String) {
        if (!_state.value.isLoading) {
            _state.update { it.copy(password = password, error = null) }
        }
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        if (!_state.value.isLoading) {
            _state.update { it.copy(confirm = passwordConfirm, error = null) }
        }
    }

    fun sendVerificationCode(onSuccess: () -> Unit = { navigationChannel.trySend(PasswordResetNavigation.Verification) }) {
        send(false, onSuccess)
    }

    fun resendVerificationCode() {
        send(true) { }
    }

    private fun send(isResend: Boolean, onSuccess: () -> Unit) {
        if (_state.value.isLoading || _state.value.email.isBlank()) {
            return
        }
        val requestEmail = _state.value.email.toEmailAddress()
        val requestGeneration = ++generation
        _state.update { it.copy(isLoading = true, error = null) }
        requestJob = viewModelScope.launch {
            val sendResult = sendEmail(requestEmail)
            if (requestGeneration != generation) {
                return@launch
            }
            when (sendResult) {
                is NetworkResult.Success -> {
                    val seconds = sendResult.data.expiresInSeconds.coerceIn(0, Long.MAX_VALUE / 1000)
                    deadline = clock.millis() + seconds * 1000
                    _state.update {
                        it.copy(
                            email = sendResult.data.email,
                            code = if (isResend) "" else it.code,
                            isLoading = false,
                            error = null,
                        )
                    }
                    updateRemainingTime()
                    countdownJob?.cancel()
                    countdownJob = viewModelScope.launch {
                        while (_state.value.verificationRemainingSeconds > 0) {
                            delay(1000)
                            updateRemainingTime()
                        }
                    }
                    onSuccess()
                }
                is NetworkResult.Failure -> fail(PasswordResetError.RequestFailed)
            }
        }
    }

    fun resetPassword(onSuccess: () -> Unit = { navigationChannel.trySend(PasswordResetNavigation.Completed) }) {
        updateRemainingTime()
        val form = _state.value
        if (form.isLoading) {
            return
        }
        if (!hasVerificationSession || form.verificationRemainingSeconds <= 0) {
            fail(PasswordResetError.Expired)
            return
        }
        if (form.code.length != 6 || !BookOnPasswordPolicy.isValid(form.password) || form.password != form.confirm) {
            return
        }
        val requestGeneration = ++generation
        _state.update { it.copy(isLoading = true, error = null) }
        requestJob = viewModelScope.launch {
            val resetResult = resetPassword(form.email.toEmailAddress(), form.code, form.password, form.confirm)
            if (requestGeneration != generation) {
                return@launch
            }
            when (resetResult) {
                is NetworkResult.Success -> {
                    clearForm()
                    onSuccess()
                }
                is NetworkResult.Failure -> fail(PasswordResetError.RequestFailed)
            }
        }
    }

    fun updateRemainingTime() {
        val remaining = deadline?.let { ((it - clock.millis()).coerceAtLeast(0) + 999) / 1000 } ?: 0
        _state.update { it.copy(verificationRemainingSeconds = remaining) }
    }

    fun cancelPendingRequest() {
        generation++
        requestJob?.cancel()
        _state.update { it.copy(isLoading = false) }
        while (navigationChannel.tryReceive().isSuccess) { }
    }

    fun clearForm() {
        cancelPendingRequest()
        countdownJob?.cancel()
        deadline = null
        _state.value = PasswordResetFormState()
    }

    private fun String.toEmailAddress(): String {
        val trimmed = trim()
        return if ('@' in trimmed) trimmed else trimmed + GSM_EMAIL_DOMAIN
    }

    private fun fail(error: PasswordResetError) {
        _state.update { it.copy(isLoading = false, error = error) }
    }
}

enum class PasswordResetNavigation { Verification, Completed }
enum class PasswordResetError { RequestFailed, Expired }

data class PasswordResetFormState(
    val email: String = "",
    val code: String = "",
    val verificationRemainingSeconds: Long = 0,
    val password: String = "",
    val confirm: String = "",
    val error: PasswordResetError? = null,
    val isLoading: Boolean = false,
)
