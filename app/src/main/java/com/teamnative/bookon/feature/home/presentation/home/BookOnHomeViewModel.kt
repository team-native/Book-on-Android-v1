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
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    private val mutableUiState = MutableStateFlow(
        emptyHomeUiState(),
    )
    val uiState: StateFlow<BookOnHomeScreenUiState> = mutableUiState.asStateFlow()

    private var loadJob: Job? = null
    private var generation = 0L

    init {
        load()
    }

    /** 느린 섹션을 기다리지 않고 완료된 서버 응답부터 반영한다. */
    fun load() {
        loadJob?.cancel()
        val requestGeneration = ++generation
        mutableUiState.update {
            it.copy(
                isInitialLoading = false,
                loadingSections = HomeSection.entries.toSet(),
                sectionErrors = emptyMap(),
                errorMessage = null,
            )
        }
        loadJob = viewModelScope.launch {
            coroutineScope {
                launch {
                    applyResponse(HomeSection.Recommendation, requestGeneration, getHome(HomeLimit)) { current, home ->
                        current.copy(
                            aiRecommendationDescription = home.todayRecommendation?.reason.orEmpty(),
                            aiRecommendedBooks = home.todayRecommendation?.let { recommendation ->
                                listOf(BookOnBookCardUiModel(
                                    recommendation.title, recommendation.author,
                                    recommendation.coverImageUrl, recommendation.bookId,
                                ))
                            }.orEmpty(),
                        )
                    }
                }
                launch {
                    applyResponse(HomeSection.Notice, requestGeneration, getNotices(FirstPage, HomeLimit)) { current, notices ->
                        current.copy(notice = notices.items.firstOrNull()?.let { notice ->
                            BookOnHomeNoticeUiModel("공지", notice.createdAt, notice.title, notice.summary)
                        })
                    }
                }
                launch {
                    applyResponse(HomeSection.Popular, requestGeneration, getBooks(FirstPage, HomeLimit, BookSort.POPULAR, null)) { current, books ->
                        current.copy(popularBooks = books.items.map { book ->
                            BookOnPopularBookRowUiModel(
                                id = book.id,
                                title = book.title,
                                metaText = "${book.author} · ${book.status}",
                                coverImageUrl = book.coverImageUrl,
                            )
                        })
                    }
                }
                launch {
                    applyResponse(HomeSection.Profile, requestGeneration, getMyProfile()) { current, profile ->
                        current.copy(userName = profile.name)
                    }
                }
            }
        }
    }

    private fun <T> applyResponse(
        section: HomeSection,
        requestGeneration: Long,
        response: NetworkResult<T>,
        updateContent: (BookOnHomeScreenUiState, T) -> BookOnHomeScreenUiState,
    ) {
        if (requestGeneration != generation) {
            return
        }
        mutableUiState.update { previous ->
            val errors = when (response) {
                is NetworkResult.Success -> previous.sectionErrors - section
                is NetworkResult.Failure -> previous.sectionErrors + (section to response.error.toUserMessage())
            }
            val content = when (response) {
                is NetworkResult.Success -> updateContent(previous, response.data)
                is NetworkResult.Failure -> previous
            }
            content.copy(
                loadingSections = previous.loadingSections - section,
                sectionErrors = errors,
                errorMessage = errors.values.firstOrNull(),
            )
        }
    }

}

private fun emptyHomeUiState() = BookOnHomeScreenUiState(
    greeting = "",
    userName = "",
    notice = null,
    aiRecommendationDescription = "",
    aiRecommendedBooks = emptyList(),
    popularBooks = emptyList(),
    newBooks = emptyList(),
)

private fun com.teamnative.bookon.core.network.NetworkError.toUserMessage(): String = when (this) {
    is com.teamnative.bookon.core.network.NetworkError.Http -> message
    else -> "홈 정보를 불러오지 못했습니다."
}
