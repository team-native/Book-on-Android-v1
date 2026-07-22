package com.teamnative.bookon.feature.auth.presentation.readingmarathonlink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.auth.domain.LinkRead365UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Read365LinkState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
)

@HiltViewModel
class BookOnReadingMarathonLinkViewModel @Inject constructor(
    private val linkRead365: LinkRead365UseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(Read365LinkState())
    val state: StateFlow<Read365LinkState> = mutableState.asStateFlow()

    /** 완료 클릭 시 Read365 계정을 연동하고 실패 문구를 현재 화면에 남긴다. */
    fun link(id: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        mutableState.value = Read365LinkState(isLoading = true)
        when (val result = linkRead365(id, password)) {
            is NetworkResult.Success -> {
                mutableState.value = Read365LinkState()
                onSuccess()
            }
            is NetworkResult.Failure -> mutableState.value = Read365LinkState(
                errorText = result.error.userMessage(),
            )
        }
    }
}

private fun NetworkError.userMessage(): String = when (this) {
    is NetworkError.Http -> message
    else -> "네트워크 연결을 확인한 뒤 다시 시도해 주세요."
}
