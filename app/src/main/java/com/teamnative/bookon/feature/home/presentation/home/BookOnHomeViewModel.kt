package com.teamnative.bookon.feature.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import com.teamnative.bookon.feature.book.domain.GetNewBooksUseCase
import com.teamnative.bookon.feature.home.domain.GetHomeUseCase
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

private const val HomeLimit = 5
private const val FirstPage = 1

/** 홈의 공개 데이터와 도서 목록을 병렬 조회해 표시 모델로 변환한다. */
@HiltViewModel
class BookOnHomeViewModel @Inject constructor(
    private val getHome: GetHomeUseCase,
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

    /** 화면 최초 진입 시 홈 추천·공지·인기·신간을 함께 조회한다. */
    /** 홈 재시도와 최초 진입 시 공개 섹션을 함께 새로 고친다. */
    fun load() = viewModelScope.launch {
        mutableUiState.value = mutableUiState.value.copy(errorMessage = null)
        val homeResult = async { getHome(HomeLimit) }
        val noticesResult = async { getNotices(FirstPage, HomeLimit) }
        val popularResult = async { getBooks(FirstPage, HomeLimit, BookSort.POPULAR, null) }
        val newBooksResult = async { getNewBooks(FirstPage, HomeLimit) }
        val profileResult = async { getMyProfile() }
        val results = listOf(homeResult.await(), noticesResult.await(), popularResult.await(), newBooksResult.await())
        val errorMessage = results.filterIsInstance<NetworkResult.Failure>().firstOrNull()?.error?.toUserMessage()
        val home = homeResult.await() as? NetworkResult.Success
        val notices = noticesResult.await() as? NetworkResult.Success
        val popular = popularResult.await() as? NetworkResult.Success
        val newBooks = newBooksResult.await() as? NetworkResult.Success
        val profile = profileResult.await() as? NetworkResult.Success
        mutableUiState.value = emptyHomeUiState().copy(
            greeting = "",
            userName = profile?.data?.name.orEmpty(),
            notice = notices?.data?.firstOrNull()?.let { notice ->
                BookOnHomeNoticeUiModel("공지", notice.createdAt, notice.title, notice.summary)
            },
            aiRecommendationDescription = home?.data?.todayRecommendation?.reason.orEmpty(),
            aiRecommendedBooks = home?.data?.todayRecommendation?.let { recommendation ->
                listOf(BookOnBookCardUiModel(recommendation.title, recommendation.author, recommendation.coverImageUrl, recommendation.bookId))
            }.orEmpty(),
            popularBooks = popular?.data?.items.orEmpty().map { book ->
                BookOnPopularBookRowUiModel(book.title, "${book.author} · ${book.status}")
            },
            newBooks = newBooks?.data?.items.orEmpty().map { book ->
                BookOnBookCardUiModel(book.title, book.author, book.coverImageUrl, book.id)
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
