package com.teamnative.bookon.feature.my.presentation.main

import com.teamnative.bookon.core.ui.model.BookOnMenuRowUiModel
import com.teamnative.bookon.core.ui.model.BookOnStatItemUiModel
import com.teamnative.bookon.core.ui.model.BookOnSwitchRowUiModel
import com.teamnative.bookon.feature.my.presentation.model.BookOnMyMarathonUiModel
import com.teamnative.bookon.feature.my.presentation.model.BookOnNotificationSettingsUiModel

fun sampleMyUiState() = BookOnMyScreenUiState(
    userNameText = "홍길동 님",
    studentInfoText = "10기 · 소프트웨어 개발과",
    stats = listOf(
        BookOnStatItemUiModel("대출 중", "3권"),
        BookOnStatItemUiModel("반납 임박", "2권"),
        BookOnStatItemUiModel("누적 대출", "23권"),
    ),
    marathon = BookOnMyMarathonUiModel(
        title = "2026 독서마라톤",
        statusText = "참여 중",
        progressText = "거북이 코스 · 42 / 50권",
        remainingText = "완주까지 8권 남았어요 · 상위 12%",
        percentText = "84%",
        linked = true,
        progress = 0.84f,
    ),
    menus = listOf(
        BookOnMenuRowUiModel("비밀번호 변경"),
        BookOnMenuRowUiModel("대출 / 반납 내역"),
        BookOnMenuRowUiModel("즐겨찾기 목록"),
        BookOnMenuRowUiModel("이용 안내"),
    ),
    notificationPanel = BookOnNotificationSettingsUiModel(
        title = "알림 설정",
        description = "받고 싶은 알림을 선택하세요",
        rows = listOf(
            BookOnSwitchRowUiModel("반납 알림", true, "반납 3일 전과 당일에 알려드려요"),
            BookOnSwitchRowUiModel("도서부 공지 알림", false, "새 공지가 올라오면 알려드려요"),
        ),
    ),
)
