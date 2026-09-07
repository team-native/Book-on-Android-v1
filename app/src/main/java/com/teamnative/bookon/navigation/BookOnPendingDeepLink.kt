package com.teamnative.bookon.navigation

/** 알림 탭 등 외부 신호로 시작된 초기 화면 목적지다. token은 같은 목적지를 다시 요청해도 반응하도록 매 요청마다 고유해야 한다. */
internal data class BookOnPendingDeepLink(
    val destination: BookOnDestination,
    val token: Long,
)

/** FCM data의 "type" 값을 실제 이동할 화면으로 변환한다. 대응 화면이 없는 타입은 null(기본 진입 화면 유지)로 수렴시킨다. */
internal fun String?.toPendingDeepLinkDestination(): BookOnDestination? = when (this) {
    "loan_due" -> BookOnDestination.LoanHistory
    // "notice": 공지 상세/목록 화면이 아직 없어 목적지를 만들 수 없다. 화면이 생기면 여기에 매핑을 추가한다.
    else -> null
}
