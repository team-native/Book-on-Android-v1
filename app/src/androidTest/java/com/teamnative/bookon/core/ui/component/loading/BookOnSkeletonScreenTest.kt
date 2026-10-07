package com.teamnative.bookon.core.ui.component.loading

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnUiMessage
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.BookOnThemeMode
import com.teamnative.bookon.feature.book.presentation.detail.view.BookOnBookDetailScreen
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailState
import com.teamnative.bookon.feature.home.presentation.home.BookOnHomeScreen
import com.teamnative.bookon.feature.home.presentation.home.sampleHomeUiState
import com.teamnative.bookon.feature.home.presentation.newbooks.BookOnNewBooksScreen
import com.teamnative.bookon.feature.home.presentation.newbooks.sampleNewBooksUiState
import com.teamnative.bookon.feature.library.presentation.library.BookOnLibraryScreen
import com.teamnative.bookon.feature.library.presentation.library.sampleLibraryUiState
import com.teamnative.bookon.feature.my.presentation.favorites.BookOnFavoriteBooksScreen
import com.teamnative.bookon.feature.my.presentation.favorites.sampleFavoriteBooksUiState
import com.teamnative.bookon.feature.my.presentation.loanhistory.BookOnLoanHistoryScreen
import com.teamnative.bookon.feature.my.presentation.loanhistory.sampleLoanHistoryUiState
import com.teamnative.bookon.feature.my.presentation.main.BookOnMyScreen
import com.teamnative.bookon.feature.my.presentation.main.sampleMyUiState
import com.teamnative.bookon.feature.ranking.presentation.ranking.BookOnRankingScreen
import com.teamnative.bookon.feature.ranking.presentation.ranking.sampleRankingUiState
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookOnSkeletonScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun loadingScreensKeepNavigationAndHideDataActions() {
        verifyLoadingScreens(BookOnThemeMode.LIGHT, 1f)
    }

    @Test
    fun darkLoadingScreensSupportNarrowWidthAndLargeFont() {
        verifyLoadingScreens(BookOnThemeMode.DARK, 2f)
    }

    @Test
    fun homeReplacesSkeletonWhenDataFinishes() {
        val homeState = mutableStateOf(sampleHomeUiState().copy(isInitialLoading = true))
        compose.setContent {
            BookOnTheme {
                BookOnHomeScreen(
                    uiState = homeState.value,
                    bottomBar = {},
                    onSearchClick = {},
                    onShowMoreClick = {},
                    onBookClick = {},
                    onNotificationClick = {},
                    onRetryClick = {},
                )
            }
        }
        compose.onNodeWithContentDescription(loadingDescription()).assertIsDisplayed()
        compose.runOnIdle {
            homeState.value = sampleHomeUiState()
        }
        compose.onNodeWithContentDescription(loadingDescription()).assertDoesNotExist()
        val searchPlaceholder = InstrumentationRegistry.getInstrumentation()
            .targetContext.getString(R.string.home_search_placeholder)
        compose.onNodeWithText(searchPlaceholder).assertIsDisplayed()
    }

    @Test
    fun favoritesLeaveSkeletonForErrorRetryAndEmptyResult() {
        val favoriteState = mutableStateOf(sampleFavoriteBooksUiState().copy(isInitialLoading = true))
        var retries = 0
        compose.setContent {
            BookOnTheme {
                BookOnFavoriteBooksScreen(
                    uiState = favoriteState.value,
                    onBackClick = {},
                    onBookClick = {},
                    onRetryClick = {
                        retries += 1
                        favoriteState.value = favoriteState.value.copy(
                            isInitialLoading = true,
                            errorMessage = null,
                        )
                    },
                    onLoadMoreClick = {},
                    onRemoveFavoriteClick = {},
                )
            }
        }
        compose.onNodeWithContentDescription(loadingDescription()).assertIsDisplayed()
        compose.runOnIdle {
            favoriteState.value = favoriteState.value.copy(
                isInitialLoading = false,
                books = emptyList(),
                errorMessage = BookOnUiMessage.Dynamic("Test network failure"),
            )
        }
        compose.onNodeWithContentDescription(loadingDescription()).assertDoesNotExist()
        compose.onNodeWithText("Test network failure").assertIsDisplayed()
        val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources
        compose.onNodeWithText(resources.getString(R.string.action_retry)).performClick()
        compose.onNodeWithContentDescription(loadingDescription()).assertIsDisplayed()
        compose.runOnIdle {
            assertEquals(1, retries)
            favoriteState.value = favoriteState.value.copy(isInitialLoading = false)
        }
        compose.onNodeWithContentDescription(loadingDescription()).assertDoesNotExist()
        compose.onNodeWithText(resources.getString(R.string.empty_favorite_books)).assertIsDisplayed()
    }

    private fun verifyLoadingScreens(themeMode: BookOnThemeMode, fontScale: Float) {
        val selectedScreen = mutableStateOf(LoadingScreen.Home)
        var navigationCount = 0
        compose.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
                BookOnTheme(themeMode = themeMode) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) {
                        LoadingContent(selectedScreen.value) {
                            navigationCount += 1
                        }
                    }
                }
            }
        }
        LoadingScreen.entries.forEachIndexed { index, loadingScreen ->
            compose.runOnIdle {
                selectedScreen.value = loadingScreen
            }
            compose.onNodeWithContentDescription(loadingDescription()).assertIsDisplayed()
            // Only the real tab or back control remains interactive during initial loading.
            compose.onAllNodes(hasClickAction()).assertCountEquals(1)
            compose.onNode(hasClickAction()).performClick()
            compose.runOnIdle {
                assertEquals(index + 1, navigationCount)
            }
            saveScreenshot("${themeMode.name.lowercase()}_${loadingScreen.name.lowercase()}")
        }
    }

    @Composable
    private fun LoadingContent(loadingScreen: LoadingScreen, onNavigate: () -> Unit) {
        val bottomBar: @Composable () -> Unit = {
            TextButton(onClick = onNavigate) {
                Text("Navigation")
            }
        }
        when (loadingScreen) {
            LoadingScreen.Home -> BookOnHomeScreen(
                uiState = sampleHomeUiState().copy(isInitialLoading = true),
                bottomBar = bottomBar,
                onSearchClick = {},
                onShowMoreClick = {},
                onBookClick = {},
                onNotificationClick = {},
                onRetryClick = {},
            )
            LoadingScreen.Library -> BookOnLibraryScreen(
                uiState = sampleLibraryUiState().copy(isInitialLoading = true),
                bottomBar = bottomBar,
                onEvent = {},
                onBookClick = {},
            )
            LoadingScreen.Ranking -> BookOnRankingScreen(
                uiState = sampleRankingUiState().copy(isInitialLoading = true),
                bottomBar = bottomBar,
                onRetryClick = {},
            )
            LoadingScreen.My -> BookOnMyScreen(
                uiState = sampleMyUiState().copy(isInitialLoading = true),
                bottomBar = bottomBar,
                onEvent = {},
            )
            LoadingScreen.NewBooks -> BookOnNewBooksScreen(
                uiState = sampleNewBooksUiState().copy(isInitialLoading = true),
                onBackClick = onNavigate,
                onRetryClick = {},
                onLoadMoreClick = {},
            )
            LoadingScreen.LoanHistory -> BookOnLoanHistoryScreen(
                uiState = sampleLoanHistoryUiState().copy(isInitialLoading = true),
                onEvent = { onNavigate() },
            )
            LoadingScreen.Favorites -> BookOnFavoriteBooksScreen(
                uiState = sampleFavoriteBooksUiState().copy(isInitialLoading = true),
                onBackClick = onNavigate,
                onBookClick = {},
                onRetryClick = {},
                onLoadMoreClick = {},
                onRemoveFavoriteClick = {},
            )
            LoadingScreen.Detail -> BookOnBookDetailScreen(
                state = BookOnBookDetailState(),
                onEvent = { onNavigate() },
            )
        }
    }

    private fun loadingDescription(): String = InstrumentationRegistry.getInstrumentation()
        .targetContext.getString(R.string.state_loading)

    private fun saveScreenshot(name: String) {
        val screenshotDirectory = File(
            InstrumentationRegistry.getInstrumentation().targetContext.getExternalFilesDir(null),
            "skeleton-screenshots",
        ).apply { mkdirs() }
        File(screenshotDirectory, "$name.png").outputStream().use { screenshotStream ->
            compose.onRoot().captureToImage().asAndroidBitmap()
                .compress(Bitmap.CompressFormat.PNG, 100, screenshotStream)
        }
    }

    private enum class LoadingScreen {
        Home, Library, Ranking, My, NewBooks, LoanHistory, Favorites, Detail,
    }
}
