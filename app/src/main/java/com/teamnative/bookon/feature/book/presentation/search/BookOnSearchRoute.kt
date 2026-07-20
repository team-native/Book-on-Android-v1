package com.teamnative.bookon.feature.book.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/** 서버 검색 전 로컬 검색어와 샘플 결과를 화면에 연결한다. */
@Composable
fun BookOnSearchRoute(onBackClick: () -> Unit, onBookClick: () -> Unit) {
    var query by remember { mutableStateOf("클린") }
    BookOnSearchScreen(
        uiState = sampleSearchUiState(query = query, empty = query.isBlank()),
        onQueryChange = { query = it },
        onBackClick = onBackClick,
        onBookClick = onBookClick,
    )
}
