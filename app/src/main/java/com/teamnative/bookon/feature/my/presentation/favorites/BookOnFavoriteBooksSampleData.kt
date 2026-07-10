package com.teamnative.bookon.feature.my.presentation.favorites

import com.teamnative.bookon.core.ui.model.BookOnBookListItemUiModel

fun sampleFavoriteBooksUiState() = BookOnFavoriteBooksScreenUiState(
    summary = "관심 도서 4권 · 대출 가능해지면 알려드려요",
    books = listOf(
        BookOnBookListItemUiModel("클린 코드", "로버트 C. 마틴 · 005.1"),
        BookOnBookListItemUiModel("클린 소프트웨어", "로버트 C. 마틴 · 005.1"),
        BookOnBookListItemUiModel("클린 코더", "로버트 C. 마틴 · 005.1"),
        BookOnBookListItemUiModel("클린 아키텍처", "로버트 C. 마틴 · 005.1"),
    ),
)
