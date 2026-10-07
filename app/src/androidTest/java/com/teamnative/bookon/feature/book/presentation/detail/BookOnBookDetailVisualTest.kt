package com.teamnative.bookon.feature.book.presentation.detail

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnThemeMode
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.R
import com.teamnative.bookon.feature.book.presentation.detail.view.BookOnBookDetailScreen
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenEvent
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenUiState
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailState
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookOnBookDetailVisualTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun availableDetailMatchesFigmaGeometryAndKeepsHeartFixed() {
        render(content())
        compose.onNodeWithTag("book_detail_favorite").assertIsDisplayed().assertIsEnabled()
        compose.onNodeWithTag("book_detail_loan").assertDoesNotExist()
        val coverBounds = compose.onNodeWithTag("book_detail_cover").fetchSemanticsNode().boundsInRoot
        assertEquals(2f / 3f, coverBounds.width / coverBounds.height, 0.01f)
        compose.onNodeWithTag("book_detail_favorite_button").assertIsDisplayed().assertTextEquals("관심도서 추가")
        val buttonBounds = compose.onNodeWithTag("book_detail_favorite_button").fetchSemanticsNode().boundsInRoot
        val actionBounds = compose.onNodeWithTag("book_detail_favorite").fetchSemanticsNode().boundsInRoot
        saveScreenshot("available")
        compose.onNodeWithTag("book_detail_content").performScrollToNode(hasText("책 소개"))
        compose.onNodeWithText("책 소개").assertIsDisplayed()
        compose.onNodeWithTag("book_detail_favorite").assertIsDisplayed()
        assertEquals(buttonBounds, compose.onNodeWithTag("book_detail_favorite_button").fetchSemanticsNode().boundsInRoot)
        assertEquals(actionBounds, compose.onNodeWithTag("book_detail_favorite").fetchSemanticsNode().boundsInRoot)
    }

    @Test
    fun unavailableDetailKeepsZeroStockWithoutLoanAction() {
        render(content().copy(loanAvailable = false, availableQuantity = 0))
        compose.onNodeWithTag("book_detail_loan").assertDoesNotExist()
        saveScreenshot("unavailable")
        compose.onNodeWithTag("book_detail_content").performScrollToNode(hasText("전체 2권\n대출 가능 0권"))
        compose.onNodeWithText("전체 2권\n대출 가능 0권").assertIsDisplayed()
    }

    @Test
    fun favoriteHeartOffersRemoveAction() {
        var event: BookOnBookDetailScreenEvent? = null
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(content = content().copy(isFavorite = true), isInitialLoading = false),
                    onEvent = { event = it },
                )
            }
        }
        compose.onNodeWithContentDescription("관심 도서에서 삭제").assertIsDisplayed().performClick()
        compose.runOnIdle { assertEquals(BookOnBookDetailScreenEvent.FavoriteClicked, event) }
        compose.onNodeWithTag("book_detail_favorite_button").assertTextEquals("관심도서 제외").performClick()
        compose.runOnIdle { assertEquals(BookOnBookDetailScreenEvent.FavoriteClicked, event) }
        saveScreenshot("favorite")
    }

    @Test
    fun pendingMutationCannotBeSubmitted() {
        render(content().copy(isFavoriteSubmitting = true))
        compose.onNodeWithTag("book_detail_favorite").assertIsNotEnabled()
        compose.onNodeWithTag("book_detail_favorite_button").assertIsNotEnabled()
        compose.onNodeWithTag("book_detail_loan").assertDoesNotExist()
    }

    @Test
    fun refreshDisablesBothFavoriteControls() {
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(
                        content = content(),
                        isInitialLoading = false,
                        isRefreshing = true,
                    ),
                    onEvent = {},
                )
            }
        }
        compose.onNodeWithTag("book_detail_favorite").assertIsNotEnabled()
        compose.onNodeWithTag("book_detail_favorite_button").assertIsNotEnabled()
    }

    @Test
    fun bothControlsReflectTheSameFavoriteState() {
        val detailContent = mutableStateOf(content())
        var favoriteClicks = 0
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(content = detailContent.value, isInitialLoading = false),
                    onEvent = { screenEvent ->
                        if (screenEvent == BookOnBookDetailScreenEvent.FavoriteClicked) {
                            favoriteClicks++
                        }
                    },
                )
            }
        }
        compose.onNodeWithTag("book_detail_favorite_button").assertTextEquals("관심도서 추가").performClick()
        compose.runOnIdle {
            assertEquals(1, favoriteClicks)
            detailContent.value = detailContent.value.copy(isFavorite = true)
        }
        compose.onNodeWithContentDescription("관심 도서에서 삭제").assertIsDisplayed().performClick()
        compose.onNodeWithTag("book_detail_favorite_button").assertTextEquals("관심도서 제외")
        compose.runOnIdle {
            assertEquals(2, favoriteClicks)
            detailContent.value = detailContent.value.copy(isFavorite = false)
        }
        compose.onNodeWithContentDescription("관심 도서로 추가").assertIsDisplayed()
        compose.onNodeWithTag("book_detail_favorite_button").assertTextEquals("관심도서 추가")
    }

    @Test
    fun missingDataAndLargeFontOnNarrowDarkScreenRemainReadable() {
        compose.setContent {
            val density = LocalDensity.current.density
            CompositionLocalProvider(LocalDensity provides Density(density, 2f)) {
                BookOnTheme(themeMode = BookOnThemeMode.DARK) {
                    Box(modifier = Modifier.width(320.dp).fillMaxHeight()) {
                        BookOnBookDetailScreen(
                            state = BookOnBookDetailState(
                                content = content().copy(
                                    title = "매우 긴 제목과 도서관 번호를 가진 책",
                                    libraryNumber = "813.7-매우긴청구기호-2026",
                                    totalQuantity = null,
                                    availableQuantity = null,
                                    intro = "",
                                ),
                                isInitialLoading = false,
                            ),
                            onEvent = {},
                        )
                    }
                }
            }
        }
        compose.onNodeWithTag("book_detail_favorite").assertIsDisplayed()
        compose.onNodeWithTag("book_detail_loan").assertDoesNotExist()
        compose.onNodeWithTag("book_detail_content").performScrollToNode(hasText("책 소개"))
        compose.onNodeWithText("책 소개가 없습니다.").assertIsDisplayed()
        compose.onNodeWithTag("book_detail_favorite_button").assertIsDisplayed().assertTextEquals("관심도서 추가")
        val heart = compose.onNodeWithTag("book_detail_favorite").fetchSemanticsNode().boundsInRoot
        val button = compose.onNodeWithTag("book_detail_favorite_button").fetchSemanticsNode().boundsInRoot
        assertTrue(heart.right <= button.left)
        saveScreenshot("dark-large-font")
    }

    @Test
    fun wideCoverKeepsWholeImageAndShadowInsideActualImageBounds() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val cover = File(context.cacheDir, "book-detail-wide.png")
        val bitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(android.graphics.Color.RED)
        cover.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
        render(content().copy(coverImageUrl = cover.toURI().toString()))
        compose.waitUntil(10_000) {
            compose.onAllNodesWithTag("book_detail_cover_image").fetchSemanticsNodes().isNotEmpty()
        }
        val slot = compose.onNodeWithTag("book_detail_cover").fetchSemanticsNode().boundsInRoot
        val image = compose.onNodeWithTag("book_detail_cover_image").fetchSemanticsNode().boundsInRoot
        assertEquals(2f, image.width / image.height, 0.01f)
        assertEquals(slot.center.x, image.center.x, 1f)
        assertEquals(slot.center.y, image.center.y, 1f)
        assertTrue(image.height < slot.height)
        saveScreenshot("wide-cover")
    }

    @Test
    fun missingCoverShowsPlaceholderWithoutImageShadow() {
        render(content().copy(coverImageUrl = null))
        compose.onNodeWithTag("book_detail_cover_placeholder").assertIsDisplayed()
        compose.onNodeWithTag("book_detail_cover_image").assertDoesNotExist()
    }

    @Test
    fun changingCoverUrlClearsPreviousImageAndUsesPlaceholder() {
        val original = content()
        val coverUrl = mutableStateOf(original.coverImageUrl)
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(
                        content = original.copy(coverImageUrl = coverUrl.value),
                        isInitialLoading = false,
                    ),
                    onEvent = {},
                )
            }
        }
        compose.waitUntil(10_000) {
            compose.onAllNodesWithTag("book_detail_cover_image").fetchSemanticsNodes().isNotEmpty()
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val missingCover = File(context.cacheDir, "absent-cover-${System.nanoTime()}.png")
        compose.runOnIdle { coverUrl.value = missingCover.toURI().toString() }
        compose.onNodeWithTag("book_detail_cover_placeholder").assertIsDisplayed()
        compose.onNodeWithTag("book_detail_cover_image").assertDoesNotExist()
    }

    @Test
    fun errorStateAllowsRetryAndBack() {
        var event: BookOnBookDetailScreenEvent? = null
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(
                        isInitialLoading = false,
                        errorMessage = BookOnUiMessage.Resource(R.string.book_detail_load_error),
                    ),
                    onEvent = { event = it },
                )
            }
        }
        compose.onNodeWithText("다시 시도").performClick()
        compose.runOnIdle { assertEquals(BookOnBookDetailScreenEvent.RetryClicked, event) }
        compose.onNodeWithContentDescription("뒤로가기").performClick()
        compose.runOnIdle { assertEquals(BookOnBookDetailScreenEvent.BackClicked, event) }
    }

    private fun render(uiState: BookOnBookDetailScreenUiState) {
        compose.setContent {
            BookOnTheme {
                BookOnBookDetailScreen(
                    state = BookOnBookDetailState(content = uiState, isInitialLoading = false),
                    onEvent = {},
                )
            }
        }
    }

    private fun content(): BookOnBookDetailScreenUiState {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val cover = File(instrumentation.targetContext.cacheDir, "book-detail-figma-cover.png")
        if (!cover.exists()) {
            instrumentation.context.assets.open("book_detail_figma_cover.png").use { source ->
                cover.outputStream().use { destination -> source.copyTo(destination) }
            }
        }
        return BookOnBookDetailScreenUiState(
            title = "토마토 컵라면",
            author = "차정은",
            coverImageUrl = cover.toURI().toString(),
            libraryNumber = "000",
            totalQuantity = 2,
            availableQuantity = 2,
            intro = "《토마토 컵라면》은 상처와 고민을 안고 살아가는 사람들이 우연한 만남을 통해 서로를 이해하고 위로받는 과정을 그린 이야기이다. 토마토 컵라면은 인물들의 추억과 마음을 이어 주는 상징적인 매개체로 등장한다. 이 책은 작은 일상의 소중함과 사람 사이의 따뜻한 관계가 삶에 큰 힘이 될 수 있다는 메시지를 전한다.",
            loanAvailable = true,
        )
    }

    private fun saveScreenshot(name: String) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val screenshot = File(context.getExternalFilesDir(null), "book-detail-$name.png")
        screenshot.outputStream().use { stream ->
            assertTrue(compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream))
        }
    }
}
