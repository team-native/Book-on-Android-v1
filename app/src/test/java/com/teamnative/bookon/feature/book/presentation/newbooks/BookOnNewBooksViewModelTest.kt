package com.teamnative.bookon.feature.book.presentation.newbooks

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.Book
import com.teamnative.bookon.feature.book.domain.BookCategory
import com.teamnative.bookon.feature.book.domain.BookDetail
import com.teamnative.bookon.feature.book.domain.BookPage
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetNewBooksUseCase
import com.teamnative.bookon.feature.book.domain.Loan
import com.teamnative.bookon.feature.home.presentation.newbooks.BookOnNewBooksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnNewBooksViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `다음 페이지가 있으면 더 보기로 결과를 누적한다`() = runTest {
        val repository = NewBooksRepository(
            pages = listOf(
                BookPage(listOf(book(1)), page = 1, hasNext = true, totalCount = 2),
                BookPage(listOf(book(2)), page = 2, hasNext = false, totalCount = 2),
            ),
        )
        val viewModel = BookOnNewBooksViewModel(GetNewBooksUseCase(repository))

        viewModel.loadNextPage()

        assertEquals(listOf(1L, 2L), viewModel.uiState.value.books.map { it.id })
        assertFalse(viewModel.uiState.value.hasNext)
    }

    @Test
    fun `마지막 페이지에서는 더 보기 요청을 추가로 보내지 않는다`() = runTest {
        val repository = NewBooksRepository(
            pages = listOf(BookPage(listOf(book(1)), page = 1, hasNext = false, totalCount = 1)),
        )
        val viewModel = BookOnNewBooksViewModel(GetNewBooksUseCase(repository))

        viewModel.loadNextPage()

        assertEquals(1, repository.newBooksRequests)
        assertTrue(viewModel.uiState.value.books.isNotEmpty())
    }

    private fun book(id: Long) = Book(
        id = id,
        title = "도서 $id",
        author = "작가",
        publisher = "출판사",
        category = "소설",
        libraryNumber = "A$id",
        coverImageUrl = null,
        loanAvailable = true,
        status = "AVAILABLE",
    )
}

private class NewBooksRepository(
    private val pages: List<BookPage>,
) : BookRepository {
    var newBooksRequests: Int = 0

    override suspend fun books(page: Int, size: Int, sort: BookSort, category: String?) = error("not used")

    override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int) = error("not used")

    override suspend fun newBooks(page: Int, size: Int): NetworkResult<BookPage> {
        newBooksRequests += 1
        return NetworkResult.Success(pages.getOrElse(page - 1) { pages.last() })
    }

    override suspend fun categories(): NetworkResult<List<BookCategory>> = error("not used")

    override suspend fun book(bookId: Long): NetworkResult<BookDetail> = error("not used")

    override suspend fun favorite(bookId: Long, favorite: Boolean): NetworkResult<Boolean> = error("not used")

    override suspend fun loan(bookId: Long): NetworkResult<Loan> = error("not used")
}
