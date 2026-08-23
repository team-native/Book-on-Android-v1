package com.teamnative.bookon.feature.library.presentation.library

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.BookCategory
import com.teamnative.bookon.feature.book.domain.BookPage
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBookCategoriesUseCase
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnLibraryViewModelTest {
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
    fun `서버 카테고리 code를 도서 목록 요청에 전달한다`() = runTest {
        val repository = LibraryRepository()
        val viewModel = BookOnLibraryViewModel(
            getBooks = GetBooksUseCase(repository),
            getBookCategories = GetBookCategoriesUseCase(repository),
        )

        viewModel.onEvent(BookOnLibraryScreenEvent.CategoryClicked("NOVEL"))

        assertEquals(listOf(null, "NOVEL"), repository.requestedCategoryCodes)
        assertEquals("NOVEL", viewModel.uiState.value.categories[1].code)
        assertEquals(true, viewModel.uiState.value.categories[1].selected)
    }
}

private class LibraryRepository : BookRepository {
    val requestedCategoryCodes = mutableListOf<String?>()

    override suspend fun books(page: Int, size: Int, sort: BookSort, category: String?): NetworkResult<BookPage> {
        requestedCategoryCodes += category
        return NetworkResult.Success(BookPage(emptyList(), page, hasNext = false, totalCount = 0))
    }

    override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int) = error("not used")

    override suspend fun newBooks(page: Int, size: Int) = error("not used")

    override suspend fun categories(): NetworkResult<List<BookCategory>> = NetworkResult.Success(
        listOf(BookCategory(categoryId = 7L, code = "NOVEL", name = "소설", bookCount = 10)),
    )

    override suspend fun todayRecommendations() = error("not used")

    override suspend fun purchaseLinks(bookId: Long) = error("not used")

    override suspend fun book(bookId: Long) = error("not used")

    override suspend fun favorite(bookId: Long, favorite: Boolean) = error("not used")

    override suspend fun loan(bookId: Long) = error("not used")

    override suspend fun extendLoan(loanId: Long) = error("not used")
}
