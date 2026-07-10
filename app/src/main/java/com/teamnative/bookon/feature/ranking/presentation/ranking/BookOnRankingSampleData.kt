package com.teamnative.bookon.feature.ranking.presentation.ranking

import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingListUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingMemberUiModel
import com.teamnative.bookon.feature.ranking.presentation.model.BookOnRankingPodiumUiModel

internal fun sampleRankingUiState() = BookOnRankingScreenUiState(
    description = "2026년 · 대출 권수 기준 · 매년 1월 1일 초기화",
    podium = BookOnRankingPodiumUiModel(
        first = BookOnRankingMemberUiModel(1, "홍길동", "3학년 · AI과", "49 권"),
        second = BookOnRankingMemberUiModel(2, "김길동", "2학년 · 소프트웨어 개발과", "46 권"),
        third = BookOnRankingMemberUiModel(3, "이길동", "1학년 · AI과", "45 권"),
    ),
    list = BookOnRankingListUiModel(
        members = listOf(
            BookOnRankingMemberUiModel(4, "정길동", "2학년 · 소프트웨어 개발과", "31권"),
            BookOnRankingMemberUiModel(5, "최길동", "1학년 · AI과", "28권"),
            BookOnRankingMemberUiModel(6, "한길동", "2학년 · 소프트웨어 개발과", "20권"),
        ),
    ),
)
