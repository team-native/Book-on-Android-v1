package com.teamnative.bookon.feature.my.presentation.loanhistory

import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel
import com.teamnative.bookon.core.ui.model.BookOnFilterChipUiModel

fun sampleLoanHistoryUiState() = BookOnLoanHistoryScreenUiState(
    filters = listOf(
        BookOnFilterChipUiModel("대출 중 2", true),
        BookOnFilterChipUiModel("반납 완료", false),
        BookOnFilterChipUiModel("전체", false),
    ),
    currentTitle = "대출 중",
    pastTitle = "지난 대출",
    currentLoans = listOf(
        BookOnBookListItemUiModel("클린 코드", "로버트 C. 마틴 · 반납 07.11", "D - 3"),
        BookOnBookListItemUiModel("클린 아키텍처", "로버트 C. 마틴 · 반납 07.17", "D - 9"),
    ),
    pastLoans = listOf(
        BookOnBookListItemUiModel("클린 소프트웨어", "로버트 C. 마틴 · 반납 03.08"),
        BookOnBookListItemUiModel("클린 코더", "로버트 C. 마틴 · 반납 04.09"),
    ),
)
