package com.teamnative.bookon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.teamnative.bookon.app.BookOnApp
import com.teamnative.bookon.theme.BookOnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookOnTheme {
                BookOnApp()
            }
        }
    }
}
