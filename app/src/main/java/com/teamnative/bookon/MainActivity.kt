package com.teamnative.bookon

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.AndroidEntryPoint
import com.teamnative.bookon.app.BookOnApp
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.notification.BookOnNotificationDisplayer
import com.teamnative.bookon.navigation.BookOnPendingDeepLink
import com.teamnative.bookon.navigation.toPendingDeepLinkDestination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val pendingDeepLinkState = mutableStateOf<BookOnPendingDeepLink?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 앱 콘텐츠를 항상 라이트로 고정하므로, 시스템 다크모드에서도 상태바·내비게이션바 아이콘이
        // 라이트 배경 위에서 잘 보이도록 아이콘 스타일도 라이트로 함께 고정한다.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        pendingDeepLinkState.value = if (savedInstanceState == null) {
            intent.toPendingDeepLink()
        } else {
            val kind = savedInstanceState.getString(PENDING_KIND)
            val bookId = savedInstanceState.getLong(PENDING_BOOK_ID).takeIf { it > 0 }
            kind.toPendingDeepLinkDestination(bookId)?.let {
                BookOnPendingDeepLink(it, savedInstanceState.getLong(PENDING_EVENT_ID))
            }
        }
        setContent {
            BookOnTheme {
                BookOnApp(
                    pendingDeepLink = pendingDeepLinkState.value,
                    onPendingDeepLinkConsumed = ::consumePendingDeepLink,
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        pendingDeepLinkState.value?.let { pending ->
            val kind = when (pending.destination) {
                com.teamnative.bookon.navigation.BookOnDestination.LoanHistory -> "loan_due"
                is com.teamnative.bookon.navigation.BookOnDestination.BookDetail -> "new_book"
                else -> null
            }
            outState.putString(PENDING_KIND, kind)
            outState.putLong(PENDING_EVENT_ID, pending.token)
            (pending.destination as? com.teamnative.bookon.navigation.BookOnDestination.BookDetail)?.let {
                outState.putLong(PENDING_BOOK_ID, it.bookId)
            }
        }
        super.onSaveInstanceState(outState)
    }

    /** 앱이 이미 실행 중일 때(FLAG_ACTIVITY_SINGLE_TOP 또는 singleTop launchMode) 새 알림 탭을 받아 대기 중인 딥링크를 갱신한다. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingDeepLinkState.value = intent.toPendingDeepLink()
    }

    /** 처리한 알림만 비우며 새로 도착한 이벤트는 유지한다. */
    private fun consumePendingDeepLink(token: Long) {
        if (pendingDeepLinkState.value?.token != token) {
            return
        }
        pendingDeepLinkState.value = null
        intent.removeExtra(BookOnNotificationDisplayer.NotificationTypeExtraKey)
        intent.removeExtra("bookId")
    }

    /**
     * FCM data의 "type" 값을 읽어 이동할 화면을 결정한다.
     * 포그라운드 수신 시 우리가 직접 붙인 extra와, 백그라운드/종료 상태에서 시스템이 알림을 자동 표시할 때
     * FCM SDK가 원본 data 페이로드를 그대로 실어 주는 extra가 동일한 키("type")를 쓰므로 한 경로로 처리된다.
     */
    private fun Intent.toPendingDeepLink(): BookOnPendingDeepLink? {
        val destination = getStringExtra(BookOnNotificationDisplayer.NotificationTypeExtraKey)
            .toPendingDeepLinkDestination(
                intent.extras?.get("bookId")?.toString()?.toLongOrNull(),
            )
            ?: return null
        return BookOnPendingDeepLink(destination, token = System.nanoTime())
    }

    private companion object {
        const val PENDING_KIND = "pending_notification_kind"
        const val PENDING_EVENT_ID = "pending_notification_event"
        const val PENDING_BOOK_ID = "pending_notification_book"
    }
}
