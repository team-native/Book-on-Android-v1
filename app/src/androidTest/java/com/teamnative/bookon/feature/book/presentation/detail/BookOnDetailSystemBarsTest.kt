package com.teamnative.bookon.feature.book.presentation.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import com.teamnative.bookon.MainActivity
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.feature.book.presentation.detail.view.BookOnBookDetailScreen
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailState
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.sampleBookDetailUiState
import java.io.File
import kotlin.math.abs
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookOnDetailSystemBarsTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun backgroundReachesPhysicalTopAndControlsRespectSystemBars() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val cover = File(instrumentation.targetContext.cacheDir, "book-detail-figma-cover.png")
        instrumentation.context.assets.open("book_detail_figma_cover.png").use { source ->
            cover.outputStream().use { destination -> source.copyTo(destination) }
        }
        render(
            BookOnBookDetailState(
                content = sampleBookDetailUiState(true).copy(
                    coverImageUrl = cover.toURI().toString(),
                    libraryNumber = "000",
                    intro = "《토마토 컵라면》은 상처와 고민을 안고 살아가는 사람들이 우연한 만남을 통해 서로를 이해하고 위로받는 과정을 그린 이야기이다. 토마토 컵라면은 인물들의 추억과 마음을 이어 주는 상징적인 매개체로 등장한다. 이 책은 작은 일상의 소중함과 사람 사이의 따뜻한 관계가 삶에 큰 힘이 될 수 있다는 메시지를 전한다.",
                ),
                isInitialLoading = false,
            ),
        )
        compose.waitUntil(10_000) {
            compose.onAllNodesWithTag("book_detail_cover_image").fetchSemanticsNodes().isNotEmpty()
        }
        verifyTopBackground("edge-to-edge")
        val systemInsets = compose.activity.window.decorView.rootWindowInsets
        val statusHeight = systemInsets.getInsets(WindowInsets.Type.statusBars()).top
        val navigationHeight = systemInsets.getInsets(WindowInsets.Type.navigationBars()).bottom
        val screenHeight = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot().let {
            val height = it.height
            it.recycle()
            height
        }
        val back = compose.onNodeWithContentDescription("뒤로가기").fetchSemanticsNode().boundsInRoot
        val favorite = compose.onNodeWithTag("book_detail_favorite").fetchSemanticsNode().boundsInRoot
        assertTrue(back.top >= statusHeight)
        assertTrue(favorite.bottom <= screenHeight - navigationHeight)
    }

    @Test
    fun errorBackgroundAlsoReachesPhysicalTop() {
        render(
            BookOnBookDetailState(
                isInitialLoading = false,
                errorMessage = BookOnUiMessage.Resource(R.string.book_detail_load_error),
            ),
        )
        verifyTopBackground("edge-error")
    }

    private fun render(state: BookOnBookDetailState) {
        compose.runOnUiThread {
            compose.activity.setContent {
                BookOnTheme {
                    BookOnBookDetailScreen(state = state, onEvent = {})
                }
            }
        }
        compose.waitForIdle()
        compose.runOnUiThread {
            val appearance = compose.activity.window.insetsController?.systemBarsAppearance ?: 0
            assertTrue(appearance and WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS != 0)
        }
    }

    private fun verifyTopBackground(name: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.uiAutomation.waitForIdle(100, 2_000)
        val screenshot = instrumentation.uiAutomation.takeScreenshot()
        val original = BitmapFactory.decodeResource(compose.activity.resources, R.drawable.book_detail_glow)
        val expected = original.getPixel(original.width / 3, 0)
        val actual = screenshot.getPixel(screenshot.width / 3, 4)
        assertTrue(abs(Color.red(expected) - Color.red(actual)) <= 8)
        assertTrue(abs(Color.green(expected) - Color.green(actual)) <= 8)
        assertTrue(abs(Color.blue(expected) - Color.blue(actual)) <= 8)
        val destination = File(instrumentation.targetContext.getExternalFilesDir(null), "book-detail-$name.png")
        destination.outputStream().use {
            assertTrue(screenshot.compress(Bitmap.CompressFormat.PNG, 100, it))
        }
        original.recycle()
        screenshot.recycle()
    }
}
