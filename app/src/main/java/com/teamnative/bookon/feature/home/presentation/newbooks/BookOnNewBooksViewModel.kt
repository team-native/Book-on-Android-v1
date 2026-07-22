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
    private var nextPage = FirstPage

    init {
        load(append = false)
    }

    /** 최초 진입과 재시도 시 첫 페이지를 조회한다. */
    fun retry() {
        if (mutableUiState.value.isInitialLoading || mutableUiState.value.isPagingLoading) return
        nextPage = FirstPage
        load(append = false)
    }

    /** 더 보기 클릭 시 다음 페이지가 있을 때만 목록 뒤에 결과를 추가한다. */
    fun loadNextPage() {
        if (!mutableUiState.value.hasNext || mutableUiState.value.isPagingLoading) return
        load(append = true)
    }

    private fun load(append: Boolean) = viewModelScope.launch {
        val previousBooks = if (append) mutableUiState.value.books else emptyList()
        mutableUiState.value = mutableUiState.value.copy(
            isInitialLoading = !append,
            isPagingLoading = append,
            errorMessage = null,
        )
        when (val result = getNewBooks(nextPage, PageSize)) {
            is NetworkResult.Success -> {
                nextPage = result.data.page + 1
                mutableUiState.value = BookOnNewBooksScreenUiState(
                    books = previousBooks + result.data.items.map { book ->
                    BookOnBookCardUiModel(book.title, book.author, book.coverImageUrl, book.id)
                    },
                    hasNext = result.data.hasNext,
                )
            }
            is NetworkResult.Failure -> mutableUiState.value = BookOnNewBooksScreenUiState(
                books = previousBooks,
                isInitialLoading = false,
                errorMessage = result.error.toUiMessage(R.string.error_load_new_books),
                hasNext = append && mutableUiState.value.hasNext,
            )
        }
    }
}

private fun com.teamnative.bookon.core.network.NetworkError.toUiMessage(
    fallbackResId: Int,
): BookOnUiMessage = when (this) {
    is com.teamnative.bookon.core.network.NetworkError.Http -> BookOnUiMessage.Dynamic(message)
    else -> BookOnUiMessage.Resource(fallbackResId)
}
