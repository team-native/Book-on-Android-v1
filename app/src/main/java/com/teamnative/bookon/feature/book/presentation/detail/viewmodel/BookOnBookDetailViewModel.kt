package com.teamnative.bookon.feature.book.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.book.domain.GetBookDetailUseCase
import com.teamnative.bookon.feature.book.domain.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val RESUME_REFRESH_INTERVAL_MS = 30_000L

data class BookOnBookDetailState(
    val content: BookOnBookDetailScreenUiState? = null,
    val isInitialLoading: Boolean = true,
    val errorMessage: BookOnUiMessage? = null,
    val isRefreshing: Boolean = false,
)

@HiltViewModel
class BookOnBookDetailViewModel @Inject constructor(
    private val getBookDetail: GetBookDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(BookOnBookDetailState())
    val state: StateFlow<BookOnBookDetailState> = _state.asStateFlow()
    private val errorEffects = Channel<BookOnUiMessage>(Channel.BUFFERED)
    val effects = errorEffects.receiveAsFlow()
    private var currentBookId: Long? = null
    private var loadJob: Job? = null
    private var mutationJob: Job? = null
    private var requestGeneration = 0L
    private var lastLoadStartedAt = Long.MIN_VALUE

    // 도서 ID의 최신 상세 정보를 불러온다.
    fun load(bookId: Long, forceRefresh: Boolean = false) {
        val sameBook = currentBookId == bookId
        if (sameBook && isMutationPending()) {
            return
        }
        if (sameBook && !forceRefresh && loadJob?.isActive == true) {
            return
        }
        if (sameBook && !forceRefresh && _state.value.content != null) {
            return
        }
        loadJob?.cancel()
        if (!sameBook) {
            mutationJob?.cancel()
            _state.value = BookOnBookDetailState()
        }
        currentBookId = bookId
        val generation = ++requestGeneration
        lastLoadStartedAt = System.nanoTime() / 1_000_000
        val previousContent = _state.value.content
        _state.value = _state.value.copy(
            isInitialLoading = previousContent == null,
            isRefreshing = previousContent != null,
            errorMessage = null,
        )
        loadJob = viewModelScope.launch {
            val response = getBookDetail(bookId)
            if (currentBookId != bookId || generation != requestGeneration) {
                return@launch
            }
            when (response) {
                is NetworkResult.Success -> {
                    val detail = response.data
                    val book = detail.book
                    _state.value = BookOnBookDetailState(
                        isInitialLoading = false,
                        content = BookOnBookDetailScreenUiState(
                            title = book.title,
                            author = book.author,
                            coverImageUrl = book.coverImageUrl,
                            libraryNumber = book.libraryNumber,
                            totalQuantity = detail.totalQuantity,
                            availableQuantity = detail.availableQuantity,
                            intro = detail.description.orEmpty(),
                            loanAvailable = book.loanAvailable,
                            isFavorite = detail.favorite,
                        ),
                    )
                }
                is NetworkResult.Failure -> {
                    val message = response.error.toUiMessage(R.string.book_detail_load_error)
                    _state.value = _state.value.copy(
                        isInitialLoading = false,
                        isRefreshing = false,
                        errorMessage = message,
                    )
                    if (previousContent != null) {
                        errorEffects.send(message)
                    }
                }
            }
        }
    }

    fun refreshOnResume(bookId: Long) {
        val elapsed = System.nanoTime() / 1_000_000 - lastLoadStartedAt
        if (currentBookId == bookId && _state.value.content != null && elapsed >= RESUME_REFRESH_INTERVAL_MS) {
            load(bookId, forceRefresh = true)
        }
    }

    // 서버가 확정한 관심 도서 상태를 화면에 반영한다.
    fun toggleFavorite() {
        val bookId = currentBookId ?: return
        val content = _state.value.content ?: return
        if (isMutationPending() || _state.value.isRefreshing) {
            return
        }
        _state.value = _state.value.copy(content = content.copy(isFavoriteSubmitting = true))
        mutationJob = viewModelScope.launch {
            val response = toggleFavoriteUseCase(bookId, !content.isFavorite)
            if (currentBookId != bookId) {
                return@launch
            }
            when (response) {
                is NetworkResult.Success -> updateContent {
                    it.copy(isFavorite = response.data, isFavoriteSubmitting = false)
                }
                is NetworkResult.Failure -> {
                    updateContent { it.copy(isFavoriteSubmitting = false) }
                    errorEffects.send(response.error.toUiMessage(R.string.book_detail_action_error))
                }
            }
        }
    }

    private fun isMutationPending(): Boolean {
        val content = _state.value.content
        return content?.isFavoriteSubmitting == true
    }

    private fun updateContent(transform: (BookOnBookDetailScreenUiState) -> BookOnBookDetailScreenUiState) {
        val content = _state.value.content ?: return
        _state.value = _state.value.copy(content = transform(content))
    }
}

private fun NetworkError.toUiMessage(fallbackResId: Int): BookOnUiMessage {
    return if (this is NetworkError.Http && message.isNotBlank()) {
        BookOnUiMessage.Dynamic(message)
    } else {
        BookOnUiMessage.Resource(fallbackResId)
    }
}
