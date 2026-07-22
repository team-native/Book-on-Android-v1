package com.teamnative.bookon.feature.book.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.GetBookDetailUseCase
import com.teamnative.bookon.feature.book.domain.RequestLoanUseCase
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoItemUiModel
import com.teamnative.bookon.feature.book.presentation.model.BookOnBookDetailInfoRowUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookOnBookDetailState(
    val content: BookOnBookDetailScreenUiState? = null,
    val isInitialLoading: Boolean = true,
    val errorMessage: String? = null,
)

@HiltViewModel
class BookOnBookDetailViewModel @Inject constructor(
    private val getBookDetail: GetBookDetailUseCase,
    private val requestLoan: RequestLoanUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(BookOnBookDetailState())
    val state: StateFlow<BookOnBookDetailState> = mutableState.asStateFlow()
    private var currentBookId: Long? = null
    /** 상세 화면 진입 시 도서 ID에 해당하는 최신 정보를 조회한다. */
    fun load(bookId: Long, forceRefresh: Boolean = false) {
        if (!forceRefresh && currentBookId == bookId && mutableState.value.content != null) return

        currentBookId = bookId
        viewModelScope.launch {
            val previousContent = mutableState.value.content
            mutableState.value = BookOnBookDetailState(
                content = previousContent,
                isInitialLoading = previousContent == null,
            )
            when (val result = getBookDetail(bookId)) {
                is NetworkResult.Success -> {
                    val book = result.data.book
                    mutableState.value = BookOnBookDetailState(
                        content = BookOnBookDetailScreenUiState(
                            title = book.title,
                            author = book.author,
                            info = BookOnBookDetailInfoRowUiModel(
                                items = listOf(
                                    BookOnBookDetailInfoItemUiModel("출판사", book.publisher),
                                    BookOnBookDetailInfoItemUiModel("분류", book.category),
                                    BookOnBookDetailInfoItemUiModel("청구기호", book.libraryNumber),
                                    BookOnBookDetailInfoItemUiModel(
                                        "위치",
                                        result.data.locationName.orEmpty(),
                                    ),
                                ),
                            ),
                            intro = result.data.description.orEmpty(),
                            loanAvailable = book.loanAvailable,
                        ),
                    )
                }

                is NetworkResult.Failure -> {
                    mutableState.value = BookOnBookDetailState(
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
        viewModelScope.launch {
            val content = mutableState.value.content ?: return@launch
            mutableState.value = mutableState.value.copy(
                content = content.copy(isSubmitting = true),
                errorMessage = null,
            )
            when (val result = requestLoan(bookId)) {
                is NetworkResult.Success -> {
                    load(bookId, forceRefresh = true)
                }

                is NetworkResult.Failure -> {
                    mutableState.value = mutableState.value.copy(
                        content = content.copy(isSubmitting = false),
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
