package com.teamnative.bookon.feature.my.presentation.loanhistory

/** 대출·반납 내역 Screen이 Route에 전달하는 사용자 의도이다. */
sealed interface BookOnLoanHistoryScreenEvent {
    data object BackClicked : BookOnLoanHistoryScreenEvent
    data class FilterClicked(val filterIndex: Int) : BookOnLoanHistoryScreenEvent
    data object BookClicked : BookOnLoanHistoryScreenEvent
}
