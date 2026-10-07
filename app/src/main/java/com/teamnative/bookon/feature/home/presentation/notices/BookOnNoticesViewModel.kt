package com.teamnative.bookon.feature.home.presentation.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.home.domain.GetNoticesUseCase
import com.teamnative.bookon.feature.home.domain.HomeNotice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookOnNoticesUiState(
    val notices: List<HomeNotice> = emptyList(),
    val isLoading: Boolean = false,
    val hasNext: Boolean = false,
    val hasError: Boolean = false,
)

@HiltViewModel
class BookOnNoticesViewModel @Inject constructor(
    private val getNotices: GetNoticesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookOnNoticesUiState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null
    private var nextPage = 1
    private var failedAppend = false

    init {
        load(false)
    }

    fun retry() {
        load(failedAppend)
    }

    fun loadNextPage() {
        if (_uiState.value.hasNext) {
            load(true)
        }
    }

    private fun load(append: Boolean) {
        if (_uiState.value.isLoading) {
            return
        }
        val page = if (append) nextPage else 1
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            when (val noticeResult = getNotices(page, 10)) {
                is NetworkResult.Success -> {
                    val previous = if (append) _uiState.value.notices else emptyList()
                    nextPage = noticeResult.data.page + 1
                    failedAppend = false
                    _uiState.value = BookOnNoticesUiState(
                        notices = (previous + noticeResult.data.items).distinctBy { it.noticeId },
                        hasNext = noticeResult.data.hasNext,
                    )
                }
                is NetworkResult.Failure -> {
                    failedAppend = append
                    _uiState.update { it.copy(isLoading = false, hasError = true) }
                }
            }
        }
    }
}
