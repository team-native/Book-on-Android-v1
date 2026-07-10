package com.teamnative.bookon.feature.home.presentation.home

import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnHomeNoticeUiModel
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel

internal fun sampleHomeUiState() = BookOnHomeScreenUiState(
    greeting = "좋은 저녁이에요",
    userName = "홍길동님",
    notice = BookOnHomeNoticeUiModel(
        category = "도서부 공지",
        dateText = "2026. 07. 01 · 도서부",
        title = "여름방학 도서 대출 기간 연장 안내",
        description = "방학 기간 동안 1인당 최대 5권, 대출 기간이 14일로 연장됩니다.",
        badgeText = "NEW",
    ),
    aiRecommendationDescription = "홍길동님의 대출 이력을 분석해 골랐어요",
    aiRecommendedBooks = listOf(
        BookOnBookCardUiModel("나미야 잡화점의 기적", "히가시노 게이고"),
        BookOnBookCardUiModel("아몬드", "손원평"),
        BookOnBookCardUiModel("오늘 밤, 세계에서 이 사랑이 사라진다 해도", "이치조 미사키"),
    ),
    popularBooks = listOf(
        BookOnPopularBookRowUiModel("소년이 온다", "한강 · 재고 3권"),
        BookOnPopularBookRowUiModel("데미안", "헤르만 헤세 · 재고 1권"),
    ),
    newBooks = listOf(
        BookOnBookCardUiModel("자몽 살구 클럽", "한로로"),
        BookOnBookCardUiModel("괴테는 모든 것을 말했다", "스즈키 유이"),
        BookOnBookCardUiModel("혼모노", "성해나"),
        BookOnBookCardUiModel("사랑하는 겉들", "이옥토"),
    ),
)
