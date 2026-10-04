package com.teamnative.bookon.feature.home.presentation.newbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.book.domain.GetNewBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

private const val FirstPage = 1
private const val PageSize = 20

/** 신간 화면 진입 시 /books/new 첫 페이지를 조회한다. */
@HiltViewModel
class BookOnNewBooksViewModel @Inject constructor(
    private val getNewBooks: GetNewBooksUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        BookOnNewBooksScreenUiState(
            books = emptyList(),
            isInitialLoading = true,
        ),
    )
    val uiState: StateFlow<BookOnNewBooksScreenUiState> = mutableUiState.asStateFlow()
    private var failedAppend = false
    private var loadJob: Job? = null
    private var nextPage = FirstPage

    init {
        load(append = false)
    }

    /** 최초 진입과 재시도 시 첫 페이지를 조회한다. */
    fun retry() {
        if (mutableUiState.value.isInitialLoading || mutableUiState.value.isPagingLoading) {
            return
        }
        if (failedAppend) {
            load(append = true)
        } else {
            nextPage = FirstPage
            load(append = false)
        }
    }

    /** 더 보기 클릭 시 다음 페이지가 있을 때만 목록 뒤에 결과를 추가한다. */
    fun loadNextPage() {
        if (!mutableUiState.value.hasNext || mutableUiState.value.isInitialLoading || mutableUiState.value.isPagingLoading) {
            return
        }
        load(append = true)
    }

    private fun load(append: Boolean) {
        if (loadJob?.isActive == true) {
            return
        }
        loadJob = viewModelScope.launch {
            val previousBooks = mutableUiState.value.books
            val page = nextPage
            mutableUiState.value = mutableUiState.value.copy(
                isInitialLoading = !append && previousBooks.isEmpty(),
                isPagingLoading = append,
                errorMessage = null,
            )
            val result = getNewBooks(
                page,
                PageSize
            )
            failedAppend = append && result is NetworkResult.Failure
            when (result) {
                is NetworkResult.Success -> {
                    nextPage = result.data.page + 1
                    mutableUiState.value = BookOnNewBooksScreenUiState(
                        books = (if (append) previousBooks else emptyList()) + result.data.items.map { book ->

                            BookOnBookCardUiModel(
                                book.title,
                                book.author,
                                book.coverImageUrl,
                                book.id
                            )
                        },
                        hasNext = result.data.hasNext,
                    )
                }
                is NetworkResult.Failure -> mutableUiState.value = BookOnNewBooksScreenUiState(
                    books = previousBooks,
                    isInitialLoading = false,
                    errorMessage = result.error.toUiMessage(R.string.error_load_new_books),
                    hasNext = mutableUiState.value.hasNext,
                )
            }
        }
    }
}

private fun com.teamnative.bookon.core.network.NetworkError.toUiMessage(
    fallbackResId: Int,
): BookOnUiMessage = when (this) {
    is com.teamnative.bookon.core.network.NetworkError.Http -> BookOnUiMessage.Dynamic(message)
    else -> BookOnUiMessage.Resource(fallbackResId)
}
