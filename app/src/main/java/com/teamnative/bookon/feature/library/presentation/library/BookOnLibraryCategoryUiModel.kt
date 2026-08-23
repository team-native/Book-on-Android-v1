package com.teamnative.bookon.feature.library.presentation.library

import androidx.compose.runtime.Immutable

/** 서버 카테고리 code를 화면 선택 이벤트까지 보존하는 도서실 카테고리 UI 모델이다. */
@Immutable
data class BookOnLibraryCategoryUiModel(
    val code: String?,
    val name: String,
    val selected: Boolean,
)
