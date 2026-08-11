package com.teamnative.bookon.feature.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import com.teamnative.bookon.feature.home.domain.GetHomeUseCase
import com.teamnative.bookon.feature.home.domain.GetNoticesUseCase
import com.teamnative.bookon.feature.home.presentation.model.BookOnHomeNoticeUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel
import com.teamnative.bookon.feature.my.domain.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val HomeLimit = 5
private const val FirstPage = 1

/** 홈의 공개 데이터와 도서 목록을 병렬 조회해 표시 모델로 변환한다. */
@HiltViewModel
class BookOnHomeViewModel @Inject constructor(
    private val getHome: GetHomeUseCase,
    private val getNotices: GetNoticesUseCase,
    private val getBooks: GetBooksUseCase,
    private val getMyProfile: GetMyProfileUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(BookOnHomeScreenUiState())
    val uiState: StateFlow<BookOnHomeScreenUiState> = mutableUiState.asStateFlow()

    init {
        loadHomeSections()
        loadUserName()
    }

    /** 최초 진입 시 각 섹션을 독립적으로 조회해 한 영역의 실패가 다른 영역을 막지 않게 한다. */
    private fun loadHomeSections() {
        loadNotice()
        loadRecommendation()
        loadPopularBooks()
    }

    /** 홈 최초 진입 시 내 프로필을 조회해 상단 헤더에 사용자 이름을 표시한다. */
    private fun loadUserName() {
        viewModelScope.launch {
            when (val result = getMyProfile()) {
                is NetworkResult.Success -> {
                    mutableUiState.update { currentState ->
                        currentState.copy(userName = result.data.name)
                    }
                }

                is NetworkResult.Failure -> {
                    // 헤더 이름을 비워 두고, 홈의 다른 섹션은 계속 표시한다.
                }
            }
        }
    }

    /** 공지 영역의 재시도 버튼에서 호출되며, 성공·빈 결과·오류를 공지 상태에만 반영한다. */
    fun retryNotice() = loadNotice()

    /** AI 추천 영역의 재시도 버튼에서 호출되며, 해당 API 결과만 추천 상태에 반영한다. */
    fun retryRecommendation() = loadRecommendation()

    /** 인기 책 영역의 재시도 버튼에서 호출되며, 해당 API 결과만 인기 책 상태에 반영한다. */
    fun retryPopularBooks() = loadPopularBooks()

    private fun loadNotice() = viewModelScope.launch {
        mutableUiState.update { currentState ->
            currentState.copy(notice = BookOnHomeSectionUiState.Loading)
        }
        when (val result = getNotices(FirstPage, HomeLimit)) {
            is NetworkResult.Success -> {
                val notice = result.data.firstOrNull()
                mutableUiState.update { currentState ->
                    currentState.copy(
                        notice = notice?.let {
                            BookOnHomeSectionUiState.Content(
                                BookOnHomeNoticeUiModel("공지", it.createdAt, it.title, it.summary),
                            )
                        } ?: BookOnHomeSectionUiState.Empty,
                    )
                }
            }

            is NetworkResult.Failure -> {
                mutableUiState.update { currentState ->
                    currentState.copy(
                        notice = BookOnHomeSectionUiState.Error(result.error.toUserMessage()),
                    )
                }
            }
        }
    }

    private fun loadRecommendation() = viewModelScope.launch {
        mutableUiState.update { currentState ->
            currentState.copy(recommendation = BookOnHomeSectionUiState.Loading)
        }
        when (val result = getHome(HomeLimit)) {
            is NetworkResult.Success -> {
                val recommendation = result.data.todayRecommendation
                mutableUiState.update { currentState ->
                    currentState.copy(
                        recommendation = recommendation?.let {
                            BookOnHomeSectionUiState.Content(
                                BookOnAiRecommendationUiModel(
                                    description = it.reason.orEmpty(),
                                    books = listOf(
                                        BookOnBookCardUiModel(
                                            it.title,
                                            it.author,
                                            it.coverImageUrl,
                                            it.bookId,
                                        ),
                                    ),
                                ),
                            )
                        } ?: BookOnHomeSectionUiState.Empty,
                    )
                }
            }

            is NetworkResult.Failure -> {
                mutableUiState.update { currentState ->
                    currentState.copy(
                        recommendation = BookOnHomeSectionUiState.Error(result.error.toUserMessage()),
                    )
                }
            }
        }
    }

    private fun loadPopularBooks() = viewModelScope.launch {
        mutableUiState.update { currentState ->
            currentState.copy(popularBooks = BookOnHomeSectionUiState.Loading)
        }
        when (val result = getBooks(FirstPage, HomeLimit, BookSort.POPULAR, null)) {
            is NetworkResult.Success -> {
                val books = result.data.items.map { book ->
                    BookOnPopularBookRowUiModel(book.title, "${book.author} · ${book.status}")
                }
                mutableUiState.update { currentState ->
                    currentState.copy(
                        popularBooks = if (books.isEmpty()) {
                            BookOnHomeSectionUiState.Empty
                        } else {
                            BookOnHomeSectionUiState.Content(books)
                        },
                    )
                }
            }

            is NetworkResult.Failure -> {
                mutableUiState.update { currentState ->
                    currentState.copy(
                        popularBooks = BookOnHomeSectionUiState.Error(result.error.toUserMessage()),
                    )
                }
            }
        }
    }
}

private fun com.teamnative.bookon.core.network.NetworkError.toUserMessage(): String = when (this) {
    is com.teamnative.bookon.core.network.NetworkError.Http -> message
    else -> "홈 정보를 불러오지 못했습니다."
}
