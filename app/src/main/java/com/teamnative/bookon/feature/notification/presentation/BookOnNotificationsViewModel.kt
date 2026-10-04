package com.teamnative.bookon.feature.notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.notification.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookOnNotificationsUiState(
    val notifications: List<BookOnNotification> = emptyList(),
    val expandedId: Long? = null,
    val isLoading: Boolean = false,
    val isMarkingAll: Boolean = false,
    val markingIds: Set<Long> = emptySet(),
    val hasNext: Boolean = false,
    val hasError: Boolean = false,
    val unreadCount: Int? = null,
)

@HiltViewModel
class BookOnNotificationsViewModel @Inject constructor(
    private val getNotifications: GetNotificationsUseCase,
    private val markRead: MarkNotificationReadUseCase,
    private val markAllRead: MarkAllNotificationsReadUseCase,
    private val getUnreadCount: GetUnreadNotificationCountUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookOnNotificationsUiState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null
    private var countJob: Job? = null
    private var loadGeneration = 0L
    private var countGeneration = 0L
    private var nextPage = 1
    private var failedAppend = false

    fun refresh() {
        if (_uiState.value.isMarkingAll || _uiState.value.markingIds.isNotEmpty()) {
            return
        }
        loadJob?.cancel()
        load(false)
        refreshCount()
    }

    fun retry() {
        if (!_uiState.value.isLoading && !_uiState.value.isMarkingAll && _uiState.value.markingIds.isEmpty()) {
            load(failedAppend)
            refreshCount()
        }
    }

    fun loadNextPage() {
        if (_uiState.value.hasNext && !_uiState.value.isLoading && !_uiState.value.isMarkingAll && _uiState.value.markingIds.isEmpty()) {
            load(true)
        }
    }

    /** 내용을 펼치고 서버에서 읽음을 확인한다. */
    fun expand(notificationId: Long) {
        _uiState.update {
            it.copy(expandedId = if (it.expandedId == notificationId) null else notificationId)
        }
        retryRead(notificationId)
    }

    fun retryRead(notificationId: Long) {
        val selected = _uiState.value.notifications.firstOrNull { it.id == notificationId } ?: return
        if (selected.isRead || notificationId in _uiState.value.markingIds || _uiState.value.isMarkingAll) {
            return
        }
        invalidateReads()
        _uiState.update { it.copy(markingIds = it.markingIds + notificationId, hasError = false) }
        viewModelScope.launch {
            val readResult = markRead(notificationId)
            val confirmed = readResult is NetworkResult.Success &&
                readResult.data.notificationId == notificationId && readResult.data.read
            _uiState.update { current ->
                current.copy(
                    markingIds = current.markingIds - notificationId,
                    notifications = if (confirmed) current.notifications.map { notification ->
                        if (notification.id == notificationId) notification.copy(isRead = true) else notification
                    } else current.notifications,
                    hasError = !confirmed,
                )
            }
            invalidateReads()
            refreshCount()
        }
    }

    fun readAll() {
        if (_uiState.value.isMarkingAll || _uiState.value.markingIds.isNotEmpty()) {
            return
        }
        invalidateReads()
        _uiState.update { it.copy(isMarkingAll = true, hasError = false) }
        viewModelScope.launch {
            when (val readAllResult = markAllRead()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isMarkingAll = false) }
                    // updated=false도 정상 처리 결과이며 최신 서버 목록으로 확정한다.
                    refresh()
                }
                is NetworkResult.Failure -> {
                    _uiState.update { it.copy(isMarkingAll = false, hasError = true) }
                    refreshCount()
                }
            }
        }
    }

    private fun invalidateReads() {
        loadGeneration++
        countGeneration++
        loadJob?.cancel()
        countJob?.cancel()
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun refreshCount() {
        countJob?.cancel()
        val generation = ++countGeneration
        countJob = viewModelScope.launch {
            val countResult = getUnreadCount()
            if (generation != countGeneration) {
                return@launch
            }
            when (countResult) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(unreadCount = countResult.data)
                }
                is NetworkResult.Failure -> _uiState.update {
                    it.copy(hasError = true)
                }
            }
        }
    }

    private fun load(append: Boolean) {
        val generation = ++loadGeneration
        val page = if (append) nextPage else 1
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            val pageResult = getNotifications(page, 20)
            if (generation != loadGeneration) {
                return@launch
            }
            when (pageResult) {
                is NetworkResult.Success -> {
                    val previous = if (append) _uiState.value.notifications else emptyList()
                    nextPage = pageResult.data.page + 1
                    failedAppend = false
                    _uiState.update {
                        it.copy(
                            notifications = (previous + pageResult.data.items).distinctBy { entry -> entry.id },
                            hasNext = pageResult.data.hasNext,
                            isLoading = false,
                        )
                    }
                }
                is NetworkResult.Failure -> {
                    failedAppend = append
                    _uiState.update { it.copy(isLoading = false, hasError = true) }
                }
            }
        }
    }
}
