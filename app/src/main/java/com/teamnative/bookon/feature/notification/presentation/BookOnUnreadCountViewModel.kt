package com.teamnative.bookon.feature.notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.notification.domain.GetUnreadNotificationCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookOnUnreadCountViewModel @Inject constructor(
    private val getUnreadCount: GetUnreadNotificationCountUseCase,
) : ViewModel() {
    private val _count = MutableStateFlow<Int?>(null)
    val count = _count.asStateFlow()
    private var loadJob: Job? = null
    private var generation = 0L

    fun refresh() {
        loadJob?.cancel()
        val requestGeneration = ++generation
        loadJob = viewModelScope.launch {
            val countResult = getUnreadCount()
            if (requestGeneration != generation) {
                return@launch
            }
            _count.value = when (countResult) {
                is NetworkResult.Success -> countResult.data
                is NetworkResult.Failure -> null
            }
        }
    }
}
