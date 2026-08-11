package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnHomeNoticeUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel

@Immutable
data class BookOnHomeScreenUiState(
    val userName: String = "",
    val notice: BookOnHomeSectionUiState<BookOnHomeNoticeUiModel> = BookOnHomeSectionUiState.Loading,
    val recommendation: BookOnHomeSectionUiState<BookOnAiRecommendationUiModel> = BookOnHomeSectionUiState.Loading,
    val popularBooks: BookOnHomeSectionUiState<List<BookOnPopularBookRowUiModel>> = BookOnHomeSectionUiState.Loading,
)

/** 홈의 독립 섹션별 서버 조회 상태를 명확히 표현한다. */
sealed interface BookOnHomeSectionUiState<out T> {
    data object Loading : BookOnHomeSectionUiState<Nothing>

    data class Content<T>(val value: T) : BookOnHomeSectionUiState<T>

    data object Empty : BookOnHomeSectionUiState<Nothing>

    data class Error(val message: String) : BookOnHomeSectionUiState<Nothing>
}

/** AI 추천 영역에 표시할 설명과 책 카드 목록을 묶는다. */
@Immutable
data class BookOnAiRecommendationUiModel(
    val description: String,
    val books: List<BookOnBookCardUiModel>,
)
