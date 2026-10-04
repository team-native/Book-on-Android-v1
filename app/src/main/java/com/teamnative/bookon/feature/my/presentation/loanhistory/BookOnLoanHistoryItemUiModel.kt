package com.teamnative.bookon.feature.my.presentation.loanhistory

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

/** 대출 목록 행에 도서 정보와 서버 대출 식별자·연장 상태를 함께 전달한다. */
@Immutable
data class BookOnLoanHistoryItemUiModel(
    val book: BookOnBookListItemUiModel,
    val loanId: Long,
    val extensionAvailable: Boolean,
    val isExtending: Boolean = false,
)
