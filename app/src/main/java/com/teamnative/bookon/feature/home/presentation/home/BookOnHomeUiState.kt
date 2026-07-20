package com.teamnative.bookon.feature.home.presentation.home

import androidx.compose.runtime.Immutable
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnHomeNoticeUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel

@Immutable
data class BookOnHomeScreenUiState(
    val greeting: String,
    val userName: String,
    val notice: BookOnHomeNoticeUiModel,
    val aiRecommendationDescription: String,
    val aiRecommendedBooks: List<BookOnBookCardUiModel>,
    val popularBooks: List<BookOnPopularBookRowUiModel>,
    val newBooks: List<BookOnBookCardUiModel>,
)
