package com.teamnative.bookon.feature.book.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.test.espresso.Espresso
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.core.ui.model.BookOnBookCardUiModel
import com.teamnative.bookon.feature.book.domain.Book
import com.teamnative.bookon.feature.book.domain.BookCategory
import com.teamnative.bookon.feature.book.domain.BookDetail
import com.teamnative.bookon.feature.book.domain.BookPage
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBookCategoriesUseCase
import com.teamnative.bookon.feature.book.domain.GetBookDetailUseCase
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import com.teamnative.bookon.feature.book.domain.GetNewBooksUseCase
import com.teamnative.bookon.feature.book.domain.Loan
import com.teamnative.bookon.feature.book.domain.RequestLoanUseCase
import com.teamnative.bookon.feature.book.domain.SearchBooksUseCase
import com.teamnative.bookon.feature.book.domain.ToggleFavoriteUseCase
import com.teamnative.bookon.feature.book.presentation.detail.view.BookOnBookDetailRoute
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailViewModel
import com.teamnative.bookon.feature.book.presentation.search.BookOnSearchRoute
import com.teamnative.bookon.feature.book.presentation.search.BookOnSearchViewModel
import com.teamnative.bookon.feature.home.presentation.component.BookOnBookSection
import com.teamnative.bookon.feature.home.presentation.newbooks.view.BookOnNewBooksRoute
import com.teamnative.bookon.feature.home.presentation.newbooks.viewmodel.BookOnNewBooksViewModel
import com.teamnative.bookon.feature.library.presentation.library.BookOnLibraryRoute
import com.teamnative.bookon.feature.library.presentation.library.BookOnLibraryViewModel
import com.teamnative.bookon.navigation.BookOnDestination
import com.teamnative.bookon.navigation.BookOnMainNavigationState
import com.teamnative.bookon.navigation.BookOnMainNavigator
import com.teamnative.bookon.navigation.mainDestinations
import com.teamnative.bookon.navigation.rememberBookOnMainNavigationState
import com.teamnative.bookon.navigation.toEntries
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private const val BOOK_ID_BASE = 8013595087L

class BookOnDetailNavigationTest {
    @get:Rule
    val compose = createComposeRule()
    private val repository = NavigationBookRepository()
    private lateinit var navigationState: BookOnMainNavigationState

    @Test
    fun searchBackPreservesQueryResultsAndScroll() {
        showSource(BookOnDestination.Search)
        compose.onNode(hasSetTextAction()).performTextInput("테스트")
        verifyScrolledRoundTrip(BookOnDestination.Home)
        compose.onNode(hasSetTextAction()).assertTextContains("테스트")
        assertEquals(1, repository.searchCalls)
    }

    @Test
    fun newBooksSystemBackPreservesListAndScroll() {
        showSource(BookOnDestination.NewBooks)
        verifyScrolledRoundTrip(BookOnDestination.Home, systemBack = true)
        assertEquals(1, repository.newBooksCalls)
    }

    @Test
    fun libraryBackKeepsOriginalTabAndList() {
        showSource(BookOnDestination.Library)
        verifyScrolledRoundTrip(BookOnDestination.Library)
        assertEquals(1, repository.libraryCalls)
    }

    @Test
    fun homeBookCallbackOpensDetailAndReturnsHome() {
        showSource(BookOnDestination.Home)
        compose.onNode(hasText("목록 1") and hasClickAction()).performClick()
        compose.onNodeWithTag("book_detail_favorite").assertIsDisplayed()
        assertEquals(BOOK_ID_BASE + 1, repository.lastDetailId)
        compose.onNodeWithContentDescription("뒤로가기").performClick()
        compose.onNodeWithText("목록 1").assertIsDisplayed()
        compose.runOnIdle {
            assertEquals(BookOnDestination.Home, navigationState.currentBackStack.last())
        }
    }

    private fun verifyScrolledRoundTrip(expectedTab: BookOnDestination, systemBack: Boolean = false) {
        compose.onNode(hasScrollAction() and SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange)).performScrollToNode(hasText("목록 12"))
        val sourceBounds = compose.onNodeWithText("목록 12").fetchSemanticsNode().boundsInRoot
        compose.onNode(hasText("목록 12") and hasClickAction()).performClick()
        compose.onNodeWithTag("book_detail_favorite").assertIsDisplayed()
        assertEquals(BOOK_ID_BASE + 12, repository.lastDetailId)
        if (systemBack) {
            Espresso.pressBack()
        } else {
            compose.onNodeWithContentDescription("뒤로가기").performClick()
        }
        compose.onNodeWithText("목록 12").assertIsDisplayed()
        assertEquals(sourceBounds, compose.onNodeWithText("목록 12").fetchSemanticsNode().boundsInRoot)
        compose.runOnIdle {
            assertEquals(expectedTab, navigationState.topLevelRoute)
            assertEquals(1, repository.detailCalls)
        }
    }

    private fun showSource(source: BookOnDestination) {
        compose.setContent {
            BookOnTheme {
                val state = rememberBookOnMainNavigationState(
                    startRoute = BookOnDestination.Home,
                    topLevelRoutes = mainDestinations,
                )
                navigationState = state
                val navigator = remember(state) { BookOnMainNavigator(state) }
                LaunchedEffect(source) {
                    if (source == BookOnDestination.Library) {
                        navigator.navigateToTab(source)
                    } else if (source != BookOnDestination.Home) {
                        navigator.push(source)
                    }
                }
                TestNavDisplay(state, navigator)
            }
        }
    }

    @Composable
    private fun TestNavDisplay(state: BookOnMainNavigationState, navigator: BookOnMainNavigator) {
        val showDetail: (Long) -> Unit = { bookId -> navigator.push(BookOnDestination.BookDetail(bookId)) }
        NavDisplay(
            entries = state.toEntries(
                entryProvider {
                    entry<BookOnDestination.Home> {
                        BookOnBookSection(
                            title = "홈 추천",
                            books = listOf(BookOnBookCardUiModel("목록 1", "저자", id = BOOK_ID_BASE + 1)),
                            onBookClick = showDetail,
                        )
                    }
                    entry<BookOnDestination.Ranking> {}
                    entry<BookOnDestination.My> {}
                    entry<BookOnDestination.Search> {
                        BookOnSearchRoute(
                            onBackClick = navigator::goBack,
                            onBookClick = showDetail,
                            viewModel = viewModel(factory = viewModelFactory {
                                initializer { BookOnSearchViewModel(SearchBooksUseCase(repository)) }
                            }),
                        )
                    }
                    entry<BookOnDestination.NewBooks> {
                        BookOnNewBooksRoute(
                            onBackClick = navigator::goBack,
                            onBookClick = showDetail,
                            viewModel = viewModel(factory = viewModelFactory {
                                initializer { BookOnNewBooksViewModel(GetNewBooksUseCase(repository)) }
                            }),
                        )
                    }
                    entry<BookOnDestination.Library> {
                        BookOnLibraryRoute(
                            bottomBar = {},
                            onBookClick = showDetail,
                            viewModel = viewModel(factory = viewModelFactory {
                                initializer {
                                    BookOnLibraryViewModel(GetBooksUseCase(repository), GetBookCategoriesUseCase(repository))
                                }
                            }),
                        )
                    }
                    entry<BookOnDestination.BookDetail> { key ->
                        BookOnBookDetailRoute(
                            bookId = key.bookId,
                            onBackClick = navigator::goBack,
                            viewModel = viewModel(factory = viewModelFactory {
                                initializer {
                                    BookOnBookDetailViewModel(
                                        GetBookDetailUseCase(repository),
                                        RequestLoanUseCase(repository),
                                        ToggleFavoriteUseCase(repository),
                                    )
                                }
                            }),
                        )
                    }
                },
            ),
            onBack = navigator::goBack,
        )
    }
}

private class NavigationBookRepository : BookRepository {
    var searchCalls = 0
    var newBooksCalls = 0
    var libraryCalls = 0
    var detailCalls = 0
    var lastDetailId: Long? = null
    private val books = (1..12).map { index ->
        Book(BOOK_ID_BASE + index, "목록 $index", "저자", "출판사", "소설", "813", null, true, "AVAILABLE")
    }
    private fun bookPage() = NetworkResult.Success(BookPage(books, 1, false, books.size))
    override suspend fun books(page: Int, size: Int, sort: BookSort, category: String?): NetworkResult<BookPage> {
        libraryCalls++
        return bookPage()
    }
    override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int): NetworkResult<BookPage> {
        searchCalls++
        return bookPage()
    }
    override suspend fun newBooks(page: Int, size: Int): NetworkResult<BookPage> {
        newBooksCalls++
        return bookPage()
    }
    override suspend fun categories() = NetworkResult.Success(listOf(BookCategory(1, "NOVEL", "소설", books.size)))
    override suspend fun book(bookId: Long): NetworkResult<BookDetail> {
        detailCalls++
        lastDetailId = bookId
        return NetworkResult.Success(BookDetail(books.first { it.id == bookId }, "소개", false, null, null, 2, 1))
    }
    override suspend fun favorite(bookId: Long, favorite: Boolean) = NetworkResult.Success(favorite)
    override suspend fun loan(bookId: Long) = NetworkResult.Success(Loan(1, bookId, "2026-10-20", "PENDING", "도서"))
    override suspend fun todayRecommendations() = error("unused")
    override suspend fun purchaseLinks(bookId: Long) = error("unused")
    override suspend fun extendLoan(loanId: Long) = error("unused")
}
