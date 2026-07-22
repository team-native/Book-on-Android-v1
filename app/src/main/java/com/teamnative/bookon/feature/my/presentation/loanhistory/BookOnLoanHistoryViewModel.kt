package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel
import com.teamnative.bookon.feature.my.domain.GetCurrentLoansUseCase
import com.teamnative.bookon.feature.my.domain.GetLoanHistoryUseCase
import com.teamnative.bookon.feature.my.domain.MyLoan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val BorrowingFilterIndex = 0
private const val ReturnedFilterIndex = 1
private const val AllFilterIndex = 2
private const val FirstPage = 1
private const val PageSize = 20
private const val ReturnedStatus = "RETURNED"

/** 대출 화면이 선택한 상태에 필요한 현재 대출과 반납 이력을 서버에서 조회한다. */
@HiltViewModel
class BookOnLoanHistoryViewModel @Inject constructor(
    private val getCurrentLoans: GetCurrentLoansUseCase,
    private val getLoanHistory: GetLoanHistoryUseCase,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(defaultUiState())
    val uiState: StateFlow<BookOnLoanHistoryScreenUiState> = mutableUiState.asStateFlow()
    private var nextHistoryPage = FirstPage
    private var hasStartedInitialLoad = false

    init {
        loadLoans(BorrowingFilterIndex)
    }

    /** 필터를 탭하면 해당 목록만 다시 요청해 불필요한 조회를 피한다. */
    fun selectFilter(filterIndex: Int) {
        if (filterIndex !in BorrowingFilterIndex..AllFilterIndex || filterIndex == mutableUiState.value.filters.indexOfFirst { it.selected }) return
        loadLoans(filterIndex)
    }

    /** 오류 화면의 재시도는 현재 선택된 필터를 그대로 다시 조회한다. */
    fun retry() {
        val selectedFilterIndex = mutableUiState.value.filters
            .indexOfFirst { it.selected }
            .coerceAtLeast(BorrowingFilterIndex)
        loadLoans(selectedFilterIndex)
    }

    /** 반납 이력이 더 있을 때 더 보기 클릭으로 다음 페이지만 목록 뒤에 추가한다. */
    fun loadMore() = viewModelScope.launch {
        val selectedFilter = mutableUiState.value.filters.indexOfFirst { it.selected }
        if (selectedFilter == BorrowingFilterIndex || !mutableUiState.value.hasNext || mutableUiState.value.isPagingLoading) return@launch
        mutableUiState.value = mutableUiState.value.copy(isPagingLoading = true, errorMessage = null)
        when (val result = getLoanHistory(nextHistoryPage, PageSize, ReturnedStatus)) {
            is NetworkResult.Success -> {
                nextHistoryPage = result.data.page + 1
                mutableUiState.value = mutableUiState.value.copy(
                    pastLoans = mutableUiState.value.pastLoans + result.data.items.map { it.toUiModel() },
                    hasNext = result.data.hasNext,
                    isPagingLoading = false,
                )
            }
            is NetworkResult.Failure -> mutableUiState.value = mutableUiState.value.copy(
                isPagingLoading = false,
                errorMessage = failureMessage(result.error),
            )
        }
    }

    private fun loadLoans(filterIndex: Int) = viewModelScope.launch {
        nextHistoryPage = FirstPage
        val shouldShowInitialLoading = !hasStartedInitialLoad
        hasStartedInitialLoad = true
        mutableUiState.value = defaultUiState(filterIndex).copy(
            isInitialLoading = shouldShowInitialLoading,
        )
        val currentResult = if (filterIndex == ReturnedFilterIndex) null else getCurrentLoans()
        val historyResult = if (filterIndex == BorrowingFilterIndex) null else getLoanHistory(FirstPage, PageSize, ReturnedStatus)
        val failure = listOfNotNull(currentResult, historyResult).filterIsInstance<NetworkResult.Failure>().firstOrNull()
        if (failure != null) {
            mutableUiState.value = defaultUiState(filterIndex).copy(errorMessage = failureMessage(failure.error))
            return@launch
        }
        val currentLoans = (currentResult as? NetworkResult.Success)?.data.orEmpty().map { it.toUiModel() }
        val historyPage = (historyResult as? NetworkResult.Success)?.data
        val pastLoans = historyPage?.items.orEmpty().map { it.toUiModel() }
        nextHistoryPage = (historyPage?.page ?: FirstPage) + 1
        mutableUiState.value = defaultUiState(filterIndex).copy(
            currentLoans = currentLoans,
            pastLoans = pastLoans,
            hasNext = historyPage?.hasNext == true,
        )
    }
}

private fun defaultUiState(selectedFilterIndex: Int = BorrowingFilterIndex) = BookOnLoanHistoryScreenUiState(
    filters = listOf("대출 중", "반납 완료", "전체").mapIndexed { index, label -> BookOnFilterChipUiModel(label, index == selectedFilterIndex) },
    currentTitle = "대출 중",
    pastTitle = "지난 대출",
    currentLoans = emptyList(),
    pastLoans = emptyList(),
)

private fun MyLoan.toUiModel() = BookOnBookListItemUiModel(
    title = title,
    metaText = dueDate?.let { "반납 예정 $it" }.orEmpty(),
    statusText = dDay?.let { "D - $it" },
    available = status != "BORROWED",
    id = bookId,
)

private fun failureMessage(error: com.teamnative.bookon.core.network.NetworkError): String = when (error) {
    is com.teamnative.bookon.core.network.NetworkError.Http -> error.message
    else -> "대출 이력을 불러오지 못했습니다. 네트워크 연결을 확인해 주세요."
}
