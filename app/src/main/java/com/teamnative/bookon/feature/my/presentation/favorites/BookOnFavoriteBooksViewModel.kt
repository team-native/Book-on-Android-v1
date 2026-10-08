package com.teamnative.bookon.feature.my.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.R
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.book.domain.ToggleFavoriteUseCase
import com.teamnative.bookon.feature.my.domain.GetFavoriteBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 관심 도서 페이지를 서버에서 불러와 화면 목록으로 변환한다. */
@HiltViewModel
class BookOnFavoriteBooksViewModel
    @Inject
    constructor(
        private val getFavoriteBooks: GetFavoriteBooksUseCase,
        private val toggleFavorite: ToggleFavoriteUseCase,
    ) : ViewModel() {
        private val mutableUiState =
            MutableStateFlow(
                BookOnFavoriteBooksScreenUiState(
                    summary = "",
                    books = emptyList(),
                    isInitialLoading = true,
                ),
            )
        val uiState: StateFlow<BookOnFavoriteBooksScreenUiState> = mutableUiState.asStateFlow()
        private var nextPage = FirstPage
        private var listJob: Job? = null
        private var requestGeneration = 0L
        private var failedAppend = false
        private var hasResumed = false
        private var lastResumeRefreshNanos = 0L
        private var shouldRefreshAfterDetail = false
        private val removingBooks = mutableSetOf<Long>()
        private val removedBooks = mutableSetOf<Long>()

        init {
            load(append = false)
        }

        /** 첫 페이지 요청을 다시 실행한다. */
        fun retry() {
            if (mutableUiState.value.isInitialLoading || mutableUiState.value.isPagingLoading) {
                return
            }
            if (failedAppend) {
                load(append = true)
            } else {
                refresh()
            }
        }

        fun onResume() {
            if (!hasResumed) {
                hasResumed = true
                lastResumeRefreshNanos = System.nanoTime()
                return
            }
            val now = System.nanoTime()
            if (!shouldRefreshAfterDetail && now - lastResumeRefreshNanos < ResumeRefreshIntervalNanos) {
                return
            }
            shouldRefreshAfterDetail = false
            lastResumeRefreshNanos = now
            refresh()
        }

        fun markDetailOpened() {
            shouldRefreshAfterDetail = true
        }

        private fun refresh() {
            listJob?.cancel()
            listJob = null
            requestGeneration += 1
            nextPage = FirstPage
            failedAppend = false
            removedBooks.clear()
            load(append = false)
        }

        /** 더 보기 클릭 시 서버가 다음 페이지를 제공할 때만 요청한다. */
        fun loadNextPage() {
            if (!mutableUiState.value.hasNext || listJob?.isActive == true) {
                return
            }
            load(append = true)
        }

        /** 관심 도서 하트 클릭 시 서버에서 관심을 해제하고 성공한 항목만 화면에서 제거한다. */
        fun removeFavorite(bookId: Long) =
            viewModelScope.launch {
                if (!removingBooks.add(bookId)) {
                    return@launch
                }
                try {
                    when (
                        val result =
                            toggleFavorite(
                                bookId,
                                favorite = false,
                            )
                    ) {
                        is NetworkResult.Success -> {
                            removedBooks.add(bookId)
                            val remainingBooks =
                                mutableUiState.value.books.filterNot {
                                    it.id == bookId
                                }
                            mutableUiState.value =
                                mutableUiState.value.copy(
                                    summary = "${remainingBooks.size}권",
                                    books = remainingBooks,
                                )
                        }

                        is NetworkResult.Failure -> {
                            mutableUiState.value =
                                mutableUiState.value.copy(
                                    errorMessage = result.error.toUiMessage(),
                                )
                        }
                    }
                } finally {
                    removingBooks.remove(bookId)
                }
            }

        private fun load(append: Boolean) {
            if (listJob?.isActive == true) {
                return
            }
            val generation = requestGeneration
            val page = nextPage
            listJob =
                viewModelScope.launch {
                    mutableUiState.value =
                        mutableUiState.value.copy(
                            isInitialLoading = !append && mutableUiState.value.books.isEmpty(),
                            isPagingLoading = append,
                            errorMessage = null,
                        )
                    val result =
                        getFavoriteBooks(
                            page = page,
                            size = PageSize,
                        )
                    if (generation != requestGeneration) {
                        return@launch
                    }
                    failedAppend = append && result is NetworkResult.Failure
                    when (result) {
                        is NetworkResult.Success -> {
                            nextPage = result.data.page + 1
                            val books =
                                (
                                    (if (append) mutableUiState.value.books else emptyList()) +
                                        result.data.items.map {
                                            BookOnBookListItemUiModel(
                                                title = it.title,
                                                metaText = "${it.author} · ${it.libraryNumber}",
                                                available = it.loanAvailable,
                                                isFavorite = true,
                                                id = it.bookId,
                                            )
                                        }
                                ).filterNot {
                                    it.id in removedBooks
                                }.distinctBy {
                                    it.id
                                }
                            mutableUiState.value =
                                BookOnFavoriteBooksScreenUiState(
                                    summary = "${books.size}권",
                                    books = books,
                                    hasNext = result.data.hasNext,
                                )
                        }

                        is NetworkResult.Failure -> {
                            mutableUiState.value =
                                mutableUiState.value.copy(
                                    isInitialLoading = false,
                                    isPagingLoading = false,
                                    errorMessage = result.error.toUiMessage(),
                                )
                        }
                    }
                }
        }
    }

private fun NetworkError.toUiMessage(): BookOnUiMessage =
    when (this) {
        is NetworkError.Http -> BookOnUiMessage.Dynamic(message)
        else -> BookOnUiMessage.Resource(R.string.error_load_favorites)
    }

private const val ResumeRefreshIntervalNanos = 1000000000L
private const val FirstPage = 1
private const val PageSize = 20
