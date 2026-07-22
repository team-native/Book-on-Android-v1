package com.teamnative.bookon.feature.book.presentation.search

import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

internal fun sampleSearchUiState(
    query: String = "클린",
    empty: Boolean = false,
): BookOnSearchScreenUiState {
    val books = if (empty) {
        emptyList()
    } else {
        listOf(
            BookOnBookListItemUiModel("클린 코드", "로버트 C. 마틴 · 005.1", "재고 2권"),
            BookOnBookListItemUiModel("클린 소프트웨어", "로버트 C. 마틴 · 005.1", "재고 3권"),
            BookOnBookListItemUiModel("클린 코더", "로버트 C. 마틴 · 005.1", "재고 1권"),
            BookOnBookListItemUiModel("클린 아키텍처", "로버트 C. 마틴 · 005.1", "재고 2권"),
        )
    }
    return BookOnSearchScreenUiState(
        query = query,
        resultSummary = "‘$query’ 검색 결과 ${books.size}건 · 제목 · 도서관 번호",
        books = books,
        emptyMessage = "검색된 책이 없어요\n다른 검색어를 입력해보세요",
    )
}
