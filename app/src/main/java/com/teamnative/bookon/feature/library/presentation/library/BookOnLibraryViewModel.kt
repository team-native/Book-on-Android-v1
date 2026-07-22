package com.teamnative.bookon.feature.library.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.book.domain.BookCategory
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBookCategoriesUseCase
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 서버 카테고리와 페이지 도서 목록을 도서실 화면 상태로 변환한다. */
@HiltViewModel
class BookOnLibraryViewModel @Inject constructor(
    private val getBooks: GetBooksUseCase,
    private val getBookCategories: GetBookCategoriesUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        BookOnLibraryScreenUiState(
            categories = emptyList(),
            sortOptions = emptyList(),
            books = emptyList(),
            isInitialLoading = true,
        ),
    )
    val uiState: StateFlow<BookOnLibraryScreenUiState> = mutableUiState.asStateFlow()

    private var categories: List<BookCategory> = emptyList()
    private var selectedCategoryIndex = AllCategoryIndex
    private var selectedSortIndex = PopularSortIndex
    private var nextPage = FirstPage

    init {
        loadCategories()
        load(append = false)
    }

    /** 필터, 재시도, 더 보기 이벤트를 현재 서버 목록 요청으로 연결한다. */
    fun onEvent(event: BookOnLibraryScreenEvent) {
        when (event) {
            is BookOnLibraryScreenEvent.CategoryClicked -> {
                selectedCategoryIndex = event.categoryIndex
                nextPage = FirstPage
                load(append = false)
            }

            is BookOnLibraryScreenEvent.SortClicked -> {
                selectedSortIndex = event.sortIndex
                nextPage = FirstPage
                load(append = false)
            }

            BookOnLibraryScreenEvent.RetryClicked -> {
                nextPage = FirstPage
                load(append = false)
            }

            BookOnLibraryScreenEvent.LoadMoreClicked -> {
                if (mutableUiState.value.hasNext && !mutableUiState.value.isPagingLoading) {
                    load(append = true)
                }
            }
        }
    }

    /** 화면 최초 진입 시 서버 카테고리를 불러와 선택 칩을 구성한다. */
    private fun loadCategories() = viewModelScope.launch {
        when (val result = getBookCategories()) {
            is NetworkResult.Success -> {
                categories = result.data
                mutableUiState.value = mutableUiState.value.copy(categories = categoryUiModels())
            }

            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                errorMessage = result.error.toUiMessage(),
            )
        }
    }

    /** 필터에 맞는 도서를 조회하고 성공 시 첫 목록 교체 또는 다음 페이지를 추가한다. */
    private fun load(append: Boolean) = viewModelScope.launch {
        val currentUiState = mutableUiState.value
        val previousBooks = if (append) currentUiState.books else emptyList()
        val shouldShowInitialLoading = !append && currentUiState.books.isEmpty()
        mutableUiState.value = currentUiState.copy(
            isInitialLoading = shouldShowInitialLoading,
            isPagingLoading = append,
            errorMessage = null,
        )
        val categoryCode = categories.getOrNull(selectedCategoryIndex - 1)?.code
        val sort = if (selectedSortIndex == PopularSortIndex) BookSort.POPULAR else BookSort.NEW

        when (val result = getBooks(nextPage, PageSize, sort, categoryCode)) {
            is NetworkResult.Success -> {
                nextPage = result.data.page + 1
                mutableUiState.value = BookOnLibraryScreenUiState(
                    categories = categoryUiModels(),
                    sortOptions = sortUiModels(),
                    books = previousBooks + result.data.items.map { book ->
                        BookOnBookCardUiModel(
                            title = book.title,
                            author = "${book.author} · ${book.status}",
                            coverImageUrl = book.coverImageUrl,
                            id = book.id,
                        )
                    },
                    hasNext = result.data.hasNext,
                )
            }

            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                isInitialLoading = false,
                isPagingLoading = false,
                errorMessage = result.error.toUiMessage(),
            )
        }
    }

    private fun categoryUiModels(): List<BookOnFilterChipUiModel> =
        listOf(BookOnFilterChipUiModel("전체", selectedCategoryIndex == AllCategoryIndex)) +
            categories.mapIndexed { index, category ->
                BookOnFilterChipUiModel(category.name, selectedCategoryIndex == index + 1)
            }

    private fun sortUiModels(): List<BookOnFilterChipUiModel> =
        listOf("인기순", "신간순").mapIndexed { index, title ->
            BookOnFilterChipUiModel(title, selectedSortIndex == index)
        }
}

private fun NetworkError.toUiMessage(): BookOnUiMessage = when (this) {
    is NetworkError.Http -> BookOnUiMessage.Dynamic(message)
    else -> BookOnUiMessage.Resource(R.string.error_load_books)
}

private const val AllCategoryIndex = 0
private const val PopularSortIndex = 0
private const val FirstPage = 1
private const val PageSize = 20
