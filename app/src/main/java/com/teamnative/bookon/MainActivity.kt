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
        // savedInstanceState가 있으면(화면 회전, 프로세스 복원 등) 같은 인텐트를 다시 해석하지 않는다.
        // 그렇지 않으면 재구성마다 같은 알림 딥링크가 화면 back stack에 중복으로 쌓인다.
        if (savedInstanceState == null) {
            pendingDeepLinkState.value = intent.toPendingDeepLink()
        }
        setContent {
            BookOnTheme {
                BookOnApp(pendingDeepLink = pendingDeepLinkState.value)
            }
        }
    }

    /** 앱이 이미 실행 중일 때(FLAG_ACTIVITY_SINGLE_TOP 또는 singleTop launchMode) 새 알림 탭을 받아 대기 중인 딥링크를 갱신한다. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingDeepLinkState.value = intent.toPendingDeepLink()
    }

    /**
     * FCM data의 "type" 값을 읽어 이동할 화면을 결정한다.
     * 포그라운드 수신 시 우리가 직접 붙인 extra와, 백그라운드/종료 상태에서 시스템이 알림을 자동 표시할 때
     * FCM SDK가 원본 data 페이로드를 그대로 실어 주는 extra가 동일한 키("type")를 쓰므로 한 경로로 처리된다.
     */
    private fun Intent.toPendingDeepLink(): BookOnPendingDeepLink? {
        val destination = getStringExtra(BookOnNotificationDisplayer.NotificationTypeExtraKey)
            .toPendingDeepLinkDestination()
            ?: return null
        return BookOnPendingDeepLink(destination, token = System.nanoTime())
    }
}
