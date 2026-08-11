package com.teamnative.bookon.feature.book.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel
import com.teamnative.bookon.feature.book.domain.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookOnSearchViewModel @Inject constructor(private val searchBooks: SearchBooksUseCase) : ViewModel() {
    private val mutableUiState = MutableStateFlow(BookOnSearchScreenUiState("", "", emptyList(), "검색어를 입력해 주세요."))
    val uiState: StateFlow<BookOnSearchScreenUiState> = mutableUiState.asStateFlow()
    private var searchJob: Job? = null
    private var nextPage = 1
    private var hasNext = false

    /** 검색어 변경 시 첫 페이지를 요청하고 이전 요청은 취소한다. */
    fun search(query: String) {
        searchJob?.cancel()
        mutableUiState.value = mutableUiState.value.copy(query = query, books = emptyList(), errorMessage = null)
        if (query.isBlank()) return
        nextPage = 1
        searchJob = viewModelScope.launch { load(query, append = false) }
    }
    fun loadNextPage() {
        if (!hasNext || mutableUiState.value.isSearching || mutableUiState.value.isPagingLoading) return

        viewModelScope.launch {
            load(mutableUiState.value.query, append = true)
        }
    }
    private suspend fun load(query: String, append: Boolean) {
        mutableUiState.value = mutableUiState.value.copy(
            isSearching = !append,
            isPagingLoading = append,
            errorMessage = null,
        )
        when (val result = searchBooks(query, null, nextPage, PageSize)) {
            is NetworkResult.Success -> {
                hasNext = result.data.hasNext
                nextPage += 1
                val books = result.data.items.map {
                    BookOnBookListItemUiModel(
                        title = it.title,
                        metaText = "${it.author} · ${it.publisher}",
                        statusText = it.status,
                        available = it.loanAvailable,
                        coverImageUrl = it.coverImageUrl,
                        id = it.id,
                    )
                }
                val combined = if (append) mutableUiState.value.books + books else books
                mutableUiState.value = mutableUiState.value.copy(
                    isSearching = false,
                    isPagingLoading = false,
                    books = combined,
                    hasNext = result.data.hasNext,
                    resultSummary = "${result.data.totalCount}권",
                    emptyMessage = "검색 결과가 없습니다.",
                )
            }
            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                isSearching = false,
                isPagingLoading = false,
                errorMessage = result.error.userMessage(),
            )
        }
    }
}
private const val PageSize = 20
private fun NetworkError.userMessage() = if (this is NetworkError.Http) message else "네트워크 연결을 확인한 뒤 다시 시도해 주세요."
