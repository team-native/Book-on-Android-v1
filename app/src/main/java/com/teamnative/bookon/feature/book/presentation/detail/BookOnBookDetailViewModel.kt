package com.teamnative.bookon.feature.book.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.GetBookDetailUseCase
import com.teamnative.bookon.feature.book.domain.RequestLoanUseCase
import com.teamnative.bookon.feature.book.domain.ToggleFavoriteUseCase
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoItemUiModel
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoRowUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

data class BookOnBookDetailState(
    val content: BookOnBookDetailScreenUiState? = null,
    val isInitialLoading: Boolean = true,
    val errorMessage: String? = null,
    val isLoanConfirmationRequired: Boolean = false,
)

@HiltViewModel
class BookOnBookDetailViewModel @Inject constructor(
    private val getBookDetail: GetBookDetailUseCase,
    private val requestLoan: RequestLoanUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(BookOnBookDetailState())
    val state: StateFlow<BookOnBookDetailState> = mutableState.asStateFlow()
    private var currentBookId: Long? = null
    private var detailJob: Job? = null
    private var requestGeneration = 0L
    /** 상세 화면 진입 시 도서 ID에 해당하는 최신 정보를 조회한다. */
    fun load(
        bookId: Long,
        forceRefresh: Boolean = false
    ) {
        if (!forceRefresh && currentBookId == bookId && mutableState.value.content != null) {
            return
        }

        detailJob?.cancel()
        requestGeneration += 1
        val generation = requestGeneration
        val requiresLoanConfirmation = mutableState.value.isLoanConfirmationRequired
        val previousContent = if (currentBookId == bookId) mutableState.value.content else null
        currentBookId = bookId
        detailJob = viewModelScope.launch {
            mutableState.value = BookOnBookDetailState(
                content = previousContent,
                isLoanConfirmationRequired = requiresLoanConfirmation,
                isInitialLoading = previousContent == null,
            )
            val result = getBookDetail(bookId)
            if (generation != requestGeneration) {
                return@launch
            }
            when (result) {
                is NetworkResult.Success -> {
                    val book = result.data.book
                    mutableState.value = BookOnBookDetailState(
                        isInitialLoading = false,
                        content = BookOnBookDetailScreenUiState(
                            title = book.title,
                            author = book.author,
                            coverImageUrl = book.coverImageUrl,
                            info = BookOnBookDetailInfoRowUiModel(
                                items = listOf(
                                    BookOnBookDetailInfoItemUiModel(
                                        "출판사",
                                        book.publisher
                                    ),
                                    BookOnBookDetailInfoItemUiModel(
                                        "분류",
                                        book.category
                                    ),
                                    BookOnBookDetailInfoItemUiModel(
                                        "청구기호",
                                        book.libraryNumber
                                    ),
                                    BookOnBookDetailInfoItemUiModel(
                                        "위치",
                                        result.data.locationName.orEmpty(),
                                    ),
                                ),
                            ),
                            intro = result.data.description.orEmpty(),
                            loanAvailable = book.loanAvailable,
                            isFavorite = result.data.favorite,
                        ),
                    )
                }

                is NetworkResult.Failure -> {
                    mutableState.value = BookOnBookDetailState(
                        content = previousContent?.copy(isSubmitting = false),
                        isLoanConfirmationRequired = requiresLoanConfirmation,
                        isInitialLoading = false,
                        errorMessage = result.error.userMessage(),
                    )
                }
            }
        }
    }
    /** 대출 버튼 클릭 시 현재 도서의 대출 신청 후 상세 정보를 새로 불러온다. */
    fun loan() {
        val bookId = currentBookId ?: return
        val content = mutableState.value.content ?: return
        if (content.isSubmitting || content.isFavoriteSubmitting || detailJob?.isActive == true || mutableState.value.isLoanConfirmationRequired) {
            return
        }
        val generation = requestGeneration
        mutableState.value = mutableState.value.copy(
            content = content.copy(isSubmitting = true),
            errorMessage = null,
        )
        viewModelScope.launch {
            val result = requestLoan(bookId)
            if (generation != requestGeneration) {
                return@launch
            }
            when (result) {
                is NetworkResult.Success -> {
                    mutableState.value = mutableState.value.copy(isLoanConfirmationRequired = true)
                    load(
                        bookId,
                        forceRefresh = true
                    )
                }

                is NetworkResult.Failure -> {
                    mutableState.value = mutableState.value.copy(
                        content = mutableState.value.content?.copy(isSubmitting = false),
                        errorMessage = result.error.userMessage(),
                    )
                }
            }
        }
    }

    /** 즐겨찾기 버튼 클릭 시 서버 상태를 변경하고 성공한 경우에만 화면 상태를 갱신한다. */
    fun toggleFavorite() {
        val bookId = currentBookId ?: return
        val content = mutableState.value.content ?: return
        if (content.isFavoriteSubmitting || content.isSubmitting || detailJob?.isActive == true) {
            return
        }

        val generation = requestGeneration
        mutableState.value = mutableState.value.copy(
            content = content.copy(isFavoriteSubmitting = true),
            errorMessage = null,
        )
        viewModelScope.launch {
            val result = toggleFavoriteUseCase(
                bookId = bookId,
                favorite = !content.isFavorite,
            )
            if (generation != requestGeneration) {
                return@launch
            }
            when (result) {
                is NetworkResult.Success -> {
                    mutableState.value = mutableState.value.copy(
                        content = mutableState.value.content?.copy(
                            isFavorite = result.data,
                            isFavoriteSubmitting = false,
                        ),
                    )
                }

                is NetworkResult.Failure -> {
                    mutableState.value = mutableState.value.copy(
                        content = mutableState.value.content?.copy(isFavoriteSubmitting = false),
                        errorMessage = result.error.userMessage(),
                    )
                }
            }
        }
    }
}
private fun NetworkError.userMessage() = if (this is NetworkError.Http) {
    message
} else {
    "네트워크 연결을 확인한 뒤 다시 시도해 주세요."
}
