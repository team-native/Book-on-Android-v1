package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

private const val LoaningFilterIndex = 0
private const val ReturnedFilterIndex = 1
private const val AllFilterIndex = 2

/** 대출·반납 샘플 상태와 필터·도서 선택 이벤트를 연결한다. */
@Composable
fun BookOnLoanHistoryRoute(
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    uiState: BookOnLoanHistoryScreenUiState = sampleLoanHistoryUiState(),
) {
    val initialSelectedFilterIndex = uiState.filters.indexOfFirst { it.selected }.coerceAtLeast(0)
    var selectedFilterIndex by rememberSaveable { mutableStateOf(initialSelectedFilterIndex) }
    val selectedUiState = uiState.copy(
        filters = uiState.filters.mapIndexed { filterIndex, filter ->
            filter.copy(selected = filterIndex == selectedFilterIndex)
        },
    )
    val displayedUiState = when (selectedFilterIndex) {
        LoaningFilterIndex -> selectedUiState.copy(
            pastLoans = emptyList(),
        )
        ReturnedFilterIndex -> selectedUiState.copy(
            currentTitle = selectedUiState.pastTitle,
            currentLoans = selectedUiState.pastLoans,
            pastLoans = emptyList(),
        )
        AllFilterIndex -> selectedUiState
        else -> selectedUiState
    }

    BookOnLoanHistoryScreen(
        uiState = displayedUiState,
        onEvent = { event ->
            when (event) {
                BookOnLoanHistoryScreenEvent.BackClicked -> onBackClick()
                is BookOnLoanHistoryScreenEvent.FilterClicked -> {
                    if (event.filterIndex in uiState.filters.indices) {
                        selectedFilterIndex = event.filterIndex
                    }
                }
                BookOnLoanHistoryScreenEvent.BookClicked -> onBookClick()
            }
        },
    )
}
