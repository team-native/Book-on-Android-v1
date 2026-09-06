package com.teamnative.bookon

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.teamnative.bookon.app.BookOnApp
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 앱 콘텐츠를 항상 라이트로 고정하므로, 시스템 다크모드에서도 상태바·내비게이션바 아이콘이
        // 라이트 배경 위에서 잘 보이도록 아이콘 스타일도 라이트로 함께 고정한다.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            BookOnTheme {
                BookOnApp()
            }
        }
    }
}
