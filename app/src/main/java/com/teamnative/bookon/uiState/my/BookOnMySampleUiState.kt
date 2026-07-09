package com.teamnative.bookon.uiState.my

import com.teamnative.bookon.ui.Component.card.BookOnStatItem
import com.teamnative.bookon.uiState.BookOnFilterChipUiState
import com.teamnative.bookon.uiState.BookOnMenuRowUiState
import com.teamnative.bookon.uiState.BookOnSwitchRowUiState
import com.teamnative.bookon.uiState.book.BookOnBookListItemUiState

/**
 * 서버 연동 전 마이페이지 Route가 사용하는 기본 상태를 만든다.
 * 이후 ViewModel이 서버 응답을 UiState로 매핑하면 이 기본값 대신 실제 상태를 전달한다.
 */
fun defaultBookOnMyScreenUiState() = BookOnMyScreenUiState(
    userNameText = "홍길동 님",
    studentInfoText = "10기 · 소프트웨어 개발과",
    stats = listOf(
        BookOnStatItem("대출 중", "3권"),
        BookOnStatItem("반납 임박", "2권"),
        BookOnStatItem("누적 대출", "23권"),
    ),
    marathon = BookOnMyMarathonUiState(
        title = "2026 독서마라톤",
        statusText = "참여 중",
        progressText = "거북이 코스 · 42 / 50권",
        remainingText = "완주까지 8권 남았어요 · 상위 12%",
        percentText = "84%",
        linked = true,
        progress = 0.84f,
    ),
    menus = listOf(
        BookOnMenuRowUiState("비밀번호 변경"),
        BookOnMenuRowUiState("대출 / 반납 내역"),
        BookOnMenuRowUiState("즐겨찾기 목록"),
        BookOnMenuRowUiState("이용 안내"),
    ),
    notificationPanel = BookOnNotificationSettingsUiState(
        title = "알림 설정",
        description = "받고 싶은 알림을 선택하세요",
        rows = listOf(
            BookOnSwitchRowUiState("반납 알림", true, "반납 3일 전과 당일에 알려드려요"),
            BookOnSwitchRowUiState("도서부 공지 알림", false, "새 공지가 올라오면 알려드려요"),
        ),
    ),
)

/**
 * 서버 연동 전 대출/반납 내역 화면이 사용하는 기본 상태를 만든다.
 */
fun defaultBookOnLoanHistoryScreenUiState() = BookOnLoanHistoryScreenUiState(
    filters = listOf(
        BookOnFilterChipUiState("대출 중 2", true),
        BookOnFilterChipUiState("반납 완료", false),
        BookOnFilterChipUiState("전체", false),
    ),
    currentTitle = "대출 중",
    pastTitle = "지난 대출",
    currentLoans = listOf(
        BookOnBookListItemUiState("클린 코드", "로버트 C. 마틴 · 반납 07.11", "D - 3"),
        BookOnBookListItemUiState("클린 아키텍처", "로버트 C. 마틴 · 반납 07.17", "D - 9"),
    ),
    pastLoans = listOf(
        BookOnBookListItemUiState("클린 소프트웨어", "로버트 C. 마틴 · 반납 03.08"),
        BookOnBookListItemUiState("클린 코더", "로버트 C. 마틴 · 반납 04.09"),
    ),
)

/**
 * 서버 연동 전 즐겨찾기 화면이 사용하는 기본 상태를 만든다.
 */
fun defaultBookOnFavoriteBooksScreenUiState() = BookOnFavoriteBooksScreenUiState(
    summary = "관심 도서 4권 · 대출 가능해지면 알려드려요",
    books = listOf(
        BookOnBookListItemUiState("클린 코드", "로버트 C. 마틴 · 005.1"),
        BookOnBookListItemUiState("클린 소프트웨어", "로버트 C. 마틴 · 005.1"),
        BookOnBookListItemUiState("클린 코더", "로버트 C. 마틴 · 005.1"),
        BookOnBookListItemUiState("클린 아키텍처", "로버트 C. 마틴 · 005.1"),
    ),
)
