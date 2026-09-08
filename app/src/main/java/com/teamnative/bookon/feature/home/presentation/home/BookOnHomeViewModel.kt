package com.teamnative.bookon.feature.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import com.teamnative.bookon.feature.book.domain.GetNewBooksUseCase
import com.teamnative.bookon.feature.book.domain.GetTodayRecommendationsUseCase
import com.teamnative.bookon.feature.home.domain.GetNoticesUseCase
import com.teamnative.bookon.feature.home.presentation.model.BookOnHomeNoticeUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel
import com.teamnative.bookon.feature.my.domain.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val HOME_LIMIT = 5
private const val FIRST_PAGE = 1

/** 홈의 공개 데이터와 도서 목록을 병렬 조회해 표시 모델로 변환한다. */
@HiltViewModel
class BookOnHomeViewModel @Inject constructor(
    private val getTodayRecommendations: GetTodayRecommendationsUseCase,
    private val getNotices: GetNoticesUseCase,
    private val getBooks: GetBooksUseCase,
    private val getNewBooks: GetNewBooksUseCase,
    private val getMyProfile: GetMyProfileUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        emptyHomeUiState().copy(isInitialLoading = true),
    )
    val uiState: StateFlow<BookOnHomeScreenUiState> = mutableUiState.asStateFlow()

    init {
        load()
    }

    /** 홈 재시도와 최초 진입 시 공개 섹션을 함께 새로 고친다. */
    fun load() = viewModelScope.launch {
        mutableUiState.value = mutableUiState.value.copy(errorMessage = null)

        val recommendationsDeferred = async {
            getTodayRecommendations()
        }
        val noticesDeferred = async {
            getNotices(FIRST_PAGE, HOME_LIMIT)
        }
        val popularBooksDeferred = async {
            getBooks(
                FIRST_PAGE,
                HOME_LIMIT,
                BookSort.POPULAR,
                null,
            )
        }
        val newBooksDeferred = async {
            getNewBooks(FIRST_PAGE, HOME_LIMIT)
        }
        val profileDeferred = async {
            getMyProfile()
        }

        val recommendationsResult = recommendationsDeferred.await()
        val noticesResult = noticesDeferred.await()
        val popularBooksResult = popularBooksDeferred.await()
        val newBooksResult = newBooksDeferred.await()
        val profileResult = profileDeferred.await()
        val results = listOf(
            recommendationsResult,
            noticesResult,
            popularBooksResult,
            newBooksResult,
            profileResult,
        )
        val errorMessage = results
            .filterIsInstance<NetworkResult.Failure>()
            .firstOrNull()
            ?.error
            ?.toUserMessage()
        val recommendations = recommendationsResult as? NetworkResult.Success
        val notices = noticesResult as? NetworkResult.Success
        val popularBooks = popularBooksResult as? NetworkResult.Success
        val newBooks = newBooksResult as? NetworkResult.Success
        val profile = profileResult as? NetworkResult.Success
        val recommendedBooks = recommendations?.data.orEmpty()

        mutableUiState.value = emptyHomeUiState().copy(
            greeting = "",
            userName = profile?.data?.name.orEmpty(),
            notice = notices?.data?.firstOrNull()?.let { notice ->
                BookOnHomeNoticeUiModel(
                    category = "공지",
                    dateText = notice.createdAt,
                    title = notice.title,
                    description = notice.summary,
                )
            },
            aiRecommendationDescription = recommendedBooks
                .firstNotNullOfOrNull { recommendation ->
                    recommendation.reason?.takeIf { reason ->
                        reason.isNotBlank()
                    }
                }
                .orEmpty(),
            aiRecommendedBooks = recommendedBooks.map { recommendation ->
                BookOnBookCardUiModel(
                    title = recommendation.title,
                    author = recommendation.author,
                    coverImageUrl = recommendation.coverImageUrl,
                    id = recommendation.bookId,
                )
            },
            popularBooks = popularBooks?.data?.items.orEmpty().map { book ->
                BookOnPopularBookRowUiModel(
                    id = book.id,
                    title = book.title,
                    metaText = "${book.author} · ${book.status}",
                    coverImageUrl = book.coverImageUrl,
                )
            },
            newBooks = newBooks?.data?.items.orEmpty().map { book ->
                BookOnBookCardUiModel(
                    title = book.title,
                    author = book.author,
                    coverImageUrl = book.coverImageUrl,
                    id = book.id,
                )
            },
            errorMessage = errorMessage,
        )
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
