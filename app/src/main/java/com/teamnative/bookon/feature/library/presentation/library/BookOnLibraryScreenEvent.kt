package com.teamnative.bookon.feature.library.presentation.library

/** 도서실 화면에서 Route로 전달하는 사용자 선택 이벤트다. */
sealed interface BookOnLibraryScreenEvent {
    /** 카테고리 칩을 선택했을 때 서버 조회에 사용할 code를 전달한다. null은 전체 카테고리다. */
    data class CategoryClicked(val categoryCode: String?) : BookOnLibraryScreenEvent

    /** 정렬 칩을 선택했을 때 해당 목록의 인덱스를 전달한다. */
    data class SortClicked(val sortIndex: Int) : BookOnLibraryScreenEvent

    data object RetryClicked : BookOnLibraryScreenEvent

    data object LoadMoreClicked : BookOnLibraryScreenEvent
}
