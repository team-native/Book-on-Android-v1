package com.teamnative.bookon.feature.home.presentation.component

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnThemeMode
import com.teamnative.bookon.feature.home.presentation.model.BookOnPopularBookRowUiModel
import java.io.File
import kotlin.math.roundToInt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookOnPopularBookShadowTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun lightCardDrawsShadowOutsideItsUnchangedBounds() {
        verifyShadow(BookOnThemeMode.LIGHT)
    }

    @Test
    fun darkCardDrawsShadowOutsideItsUnchangedBounds() {
        verifyShadow(BookOnThemeMode.DARK)
    }

    @Test
    fun popularSectionRendersShadowWithoutChangingClickTargets() {
        val clickedBookIds = mutableListOf<Long>()
        compose.setContent {
            BookOnTheme {
                Box(
                    modifier = Modifier
                        .size(
                            width = 340.dp,
                            height = 170.dp,
                        )
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    BookOnPopularBooksSection(
                        title = "우리 학교 인기 책",
                        books = listOf(
                            BookOnPopularBookRowUiModel(
                                id = 1L,
                                title = "소년이 온다",
                                metaText = "한강 · 재고 3권",
                            ),
                            BookOnPopularBookRowUiModel(
                                id = 2L,
                                title = "데미안",
                                metaText = "헤르만 헤세 · 재고 1권",
                            ),
                        ),
                        onBookClick = { clickedBookIds.add(it) },
                    )
                }
            }
        }
        compose.onNodeWithText("소년이 온다").performClick()
        compose.runOnIdle {
            assertEquals(listOf(1L), clickedBookIds)
        }
        saveScreenshot("popular_section")
    }

    private fun verifyShadow(themeMode: BookOnThemeMode) {
        var densityScale = 1f
        var backgroundColor = Color.Unspecified
        compose.setContent {
            BookOnTheme(themeMode = themeMode) {
                densityScale = LocalDensity.current.density
                backgroundColor = MaterialTheme.colorScheme.background
                CardFrame()
            }
        }
        val cardBounds = compose.onNodeWithTag("popular_card").fetchSemanticsNode().boundsInRoot
        assertEquals(
            206f * densityScale,
            cardBounds.width,
            1f,
        )
        assertEquals(
            76f * densityScale,
            cardBounds.height,
            1f,
        )
        val rootBounds = compose.onRoot().fetchSemanticsNode().boundsInRoot
        val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
        val probeX = (cardBounds.left - rootBounds.left - 4f * densityScale).roundToInt()
        val probeY = (cardBounds.center.y - rootBounds.top).roundToInt()
        val shadowColor = Color(bitmap.getPixel(probeX, probeY))
        assertTrue(
            "Card shadow must remain visible outside its clip",
            shadowColor.luminance() < backgroundColor.luminance(),
        )
        saveScreenshot("popular_card_${themeMode.name.lowercase()}")
    }

    @Composable
    private fun CardFrame() {
        Box(
            modifier = Modifier
                .size(
                    width = 242.dp,
                    height = 112.dp,
                )
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            BookOnPopularBookRow(
                title = "소년이 온다",
                metaText = "한강 · 재고 3권",
                modifier = Modifier.testTag("popular_card"),
            )
        }
    }

    private fun saveScreenshot(name: String) {
        val screenshotDirectory = File(
            InstrumentationRegistry.getInstrumentation().targetContext.getExternalFilesDir(null),
            "figma-shadow-screenshots",
        ).apply { mkdirs() }
        File(screenshotDirectory, "$name.png").outputStream().use { screenshotStream ->
            assertTrue(
                compose.onRoot().captureToImage().asAndroidBitmap()
                    .compress(Bitmap.CompressFormat.PNG, 100, screenshotStream),
            )
        }
    }
}
