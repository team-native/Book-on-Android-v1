package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.*
import com.teamnative.bookon.feature.book.presentation.search.BookOnSearchViewModel
import com.teamnative.bookon.feature.home.presentation.newbooks.BookOnNewBooksViewModel
import com.teamnative.bookon.feature.library.presentation.library.BookOnLibraryViewModel
import com.teamnative.bookon.feature.library.presentation.library.BookOnLibraryScreenEvent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class DiscoveryStateRecoveryTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() {
        Dispatchers.setMain(dispatcher)
    }
    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `clearing query ends all loading and rejects a late response`() = runTest {
        val old = CompletableDeferred<NetworkResult<BookPage>>()
        val repository = RecoveryBookRepository().apply {
            onSearch = { _, _ ->

                withContext(NonCancellable) {
                    old.await()
                }
            }
        }
        val viewModel = BookOnSearchViewModel(SearchBooksUseCase(repository))
        viewModel.search("old")
        runCurrent()
        assertTrue(viewModel.uiState.value.isSearching)
        viewModel.search("")
        old.complete(page(1L))
        runCurrent()
        assertEquals(
            "",
            viewModel.uiState.value.query
        )
        assertEquals(
            "",
            viewModel.uiState.value.resultSummary
        )
        assertTrue(viewModel.uiState.value.books.isEmpty())
        assertFalse(viewModel.uiState.value.isSearching)
        assertFalse(viewModel.uiState.value.isPagingLoading)
        assertFalse(viewModel.uiState.value.hasNext)
    }

    @Test
    fun `old search page never appends to a new query`() = runTest {
        val old = CompletableDeferred<NetworkResult<BookPage>>()
        val repository = RecoveryBookRepository().apply {
            onSearch = { query, page ->

                if (query == "a" && page == 2) withContext(NonCancellable) {
                    old.await()
                }
                else if (query == "a") page(
                    1L,
                    hasNext = true
                ) else page(3L)
            }
        }
        val viewModel = BookOnSearchViewModel(SearchBooksUseCase(repository))
        viewModel.search("a")
        runCurrent()
        viewModel.loadNextPage()
        runCurrent()
        viewModel.search("b")
        runCurrent()
        old.complete(page(2L))
        runCurrent()
        assertEquals(
            listOf(3L),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
    }

    @Test
    fun `search page retry preserves existing results and requests failed page`() = runTest {
        val requests = mutableListOf<Int>()
        var fail = true
        val repository = RecoveryBookRepository().apply {
            onSearch = { _, requestedPage ->

                requests += requestedPage
                if (requestedPage == 1) page(
                    1L,
                    hasNext = true
                )
                else if (fail) failure() else page(2L)
            }
        }
        val viewModel = BookOnSearchViewModel(SearchBooksUseCase(repository))
        viewModel.search("a")
        runCurrent()
        viewModel.loadNextPage()
        runCurrent()
        assertEquals(
            listOf(1L),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
        fail = false
        viewModel.retry()
        runCurrent()
        assertEquals(
            listOf(
                1,
                2,
                2
            ),
            requests
        )
        assertEquals(
            listOf(
                1L,
                2L
            ),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
    }

    @Test
    fun `new books failed page retry retains the first page`() = runTest {
        val requests = mutableListOf<Int>()
        var fail = true
        val repository = RecoveryBookRepository().apply {
            onNewBooks = { requestedPage ->

                requests += requestedPage
                if (requestedPage == 1) page(
                    1L,
                    hasNext = true
                )
                else if (fail) failure() else page(2L)
            }
        }
        val viewModel = BookOnNewBooksViewModel(GetNewBooksUseCase(repository))
        runCurrent()
        viewModel.loadNextPage()
        runCurrent()
        assertEquals(
            listOf(1L),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
        fail = false
        viewModel.retry()
        runCurrent()
        assertEquals(
            listOf(
                1,
                2,
                2
            ),
            requests
        )
        assertEquals(
            listOf(
                1L,
                2L
            ),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
    }

    @Test
    fun `library old filter response cannot overwrite current filter`() = runTest {
        val old = CompletableDeferred<NetworkResult<BookPage>>()
        val repository = RecoveryBookRepository().apply {
            onBooks = { _, category ->

                if (category == "old") withContext(NonCancellable) {
                    old.await()
                } else page(2L)
            }
        }
        val viewModel = BookOnLibraryViewModel(
            GetBooksUseCase(repository),
            GetBookCategoriesUseCase(repository)
        )
        runCurrent()
        viewModel.onEvent(BookOnLibraryScreenEvent.CategoryClicked("old"))
        runCurrent()
        viewModel.onEvent(BookOnLibraryScreenEvent.CategoryClicked(null))
        runCurrent()
        old.complete(page(1L))
        runCurrent()
        assertEquals(
            listOf(2L),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
        assertTrue(viewModel.uiState.value.categories.first().selected)
    }

    @Test
    fun `category failure survives successful books and can be retried`() = runTest {
        var categoriesFail = true
        var categoryCalls = 0
        val repository = RecoveryBookRepository().apply {
            onCategories = {
                categoryCalls++
                if (categoriesFail) failure() else NetworkResult.Success(listOf(BookCategory(
                            1L,
                            "100",
                            "category",
                            1
                )))
            }
        }
        val viewModel = BookOnLibraryViewModel(
            GetBooksUseCase(repository),
            GetBookCategoriesUseCase(repository)
        )
        runCurrent()
        assertNotNull(viewModel.uiState.value.errorMessage)
        categoriesFail = false
        viewModel.onEvent(BookOnLibraryScreenEvent.RetryClicked)
        runCurrent()
        assertEquals(
            2,
            categoryCalls
        )
        assertEquals(
            2,
            viewModel.uiState.value.categories.size
        )
        assertNull(viewModel.uiState.value.errorMessage)
    }

}
