package com.teamnative.bookon.feature.book.presentation.detail

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.Book
import com.teamnative.bookon.feature.book.domain.BookDetail
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBookDetailUseCase
import com.teamnative.bookon.feature.book.domain.RequestLoanUseCase
import com.teamnative.bookon.feature.book.domain.ToggleFavoriteUseCase
import com.teamnative.bookon.feature.book.domain.Loan
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnBookDetailViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `성공하면 로딩을 종료하고 서버 수량을 표시한다`() = runTest {
        val repository = DetailRepository()
        val viewModel = createViewModel(repository)
        viewModel.load(8013595087L)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isInitialLoading)
        assertEquals("책 8013595087", viewModel.state.value.content?.title)
        assertEquals(3, viewModel.state.value.content?.totalQuantity)
        assertEquals(0, viewModel.state.value.content?.availableQuantity)
    }

    @Test
    fun `최초 실패 후 재시도하고 재조회 실패에는 콘텐츠를 유지한다`() = runTest {
        val repository = DetailRepository()
        repository.failLoad = true
        val viewModel = createViewModel(repository)
        viewModel.load(1)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isInitialLoading)
        assertNotNull(viewModel.state.value.errorMessage)
        repository.failLoad = false
        viewModel.load(1)
        advanceUntilIdle()
        val content = viewModel.state.value.content
        assertNotNull(content)
        repository.failLoad = true
        viewModel.load(1, forceRefresh = true)
        advanceUntilIdle()
        assertEquals(content, viewModel.state.value.content)
        assertFalse(viewModel.state.value.isRefreshing)
    }

    @Test
    fun `관심 요청 중에는 관심 중복과 대출을 차단한다`() = runTest {
        val repository = DetailRepository()
        val viewModel = createViewModel(repository)
        viewModel.load(1)
        advanceUntilIdle()
        repository.favoriteGate = CompletableDeferred()
        viewModel.toggleFavorite()
        viewModel.toggleFavorite()
        viewModel.loan()
        runCurrent()
        assertEquals(1, repository.favoriteCalls)
        assertEquals(0, repository.loanCalls)
        repository.favoriteGate?.complete(true)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.content!!.isFavorite)
        assertFalse(viewModel.state.value.content!!.isFavoriteSubmitting)
        viewModel.toggleFavorite()
        advanceUntilIdle()
        // 서버가 true를 확정하면 요청한 false 대신 서버값을 사용한다.
        assertTrue(viewModel.state.value.content!!.isFavorite)
    }

    @Test
    fun `대출 성공 후 재조회 실패에도 제출 상태가 남지 않는다`() = runTest {
        val repository = DetailRepository()
        val viewModel = createViewModel(repository)
        viewModel.load(1)
        advanceUntilIdle()
        repository.failLoad = true
        viewModel.loan()
        advanceUntilIdle()
        assertNotNull(viewModel.state.value.content)
        assertFalse(viewModel.state.value.content!!.isSubmitting)
        assertTrue(viewModel.state.value.isLoanStateUnconfirmed)
        viewModel.loan()
        advanceUntilIdle()
        assertEquals(1, repository.loanCalls)
        repository.failLoad = false
        viewModel.load(1, forceRefresh = true)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isLoanStateUnconfirmed)
        assertFalse(viewModel.state.value.isRefreshing)
    }

    @Test
    fun `다른 책 진입은 이전 조회를 취소하고 새 책만 표시한다`() = runTest {
        val repository = DetailRepository()
        repository.detailGate = CompletableDeferred()
        val viewModel = createViewModel(repository)
        viewModel.load(1)
        runCurrent()
        repository.detailGate = null
        viewModel.load(2)
        advanceUntilIdle()
        assertEquals("책 2", viewModel.state.value.content?.title)
        assertNull(viewModel.state.value.errorMessage)
    }


    @Test
    fun `관심과 대출 실패는 기존 정보와 버튼 상태를 복원한다`() = runTest {
        val repository = DetailRepository()
        val viewModel = createViewModel(repository)
        viewModel.load(1)
        advanceUntilIdle()
        repository.failMutation = true
        viewModel.toggleFavorite()
        advanceUntilIdle()
        assertFalse(viewModel.state.value.content!!.isFavorite)
        assertFalse(viewModel.state.value.content!!.isFavoriteSubmitting)
        viewModel.loan()
        advanceUntilIdle()
        assertFalse(viewModel.state.value.content!!.isSubmitting)
        assertTrue(viewModel.state.value.content!!.loanAvailable)
        assertFalse(viewModel.state.value.isLoanStateUnconfirmed)
    }

    @Test
    fun `대출 중 연속 클릭과 관심 요청을 차단한다`() = runTest {
        val repository = DetailRepository()
        val viewModel = createViewModel(repository)
        viewModel.load(1)
        advanceUntilIdle()
        repository.loanGate = CompletableDeferred()
        viewModel.loan()
        viewModel.loan()
        viewModel.toggleFavorite()
        runCurrent()
        assertEquals(1, repository.loanCalls)
        assertEquals(0, repository.favoriteCalls)
        repository.loanGate!!.complete(Unit)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.content!!.isSubmitting)
    }

    private fun createViewModel(repository: BookRepository) = BookOnBookDetailViewModel(
        GetBookDetailUseCase(repository),
        RequestLoanUseCase(repository),
        ToggleFavoriteUseCase(repository),
    )
}

private class DetailRepository : BookRepository {
    var failLoad = false
    var failMutation = false
    var loanGate: CompletableDeferred<Unit>? = null
    var favoriteCalls = 0
    var loanCalls = 0
    var favoriteGate: CompletableDeferred<Boolean>? = null
    var detailGate: CompletableDeferred<Unit>? = null
    override suspend fun book(bookId: Long): NetworkResult<BookDetail> {
        detailGate?.await()
        if (failLoad) {
            return NetworkResult.Failure(NetworkError.Network(IllegalStateException()))
        }
        return NetworkResult.Success(
            BookDetail(
                Book(bookId, "책 $bookId", "저자", "출판사", "분류", "813", null, true, ""),
                description = null,
                favorite = false,
                locationName = null,
                returnPlanDate = null,
                totalQuantity = 3,
                availableQuantity = 0,
            ),
        )
    }
    override suspend fun favorite(bookId: Long, favorite: Boolean): NetworkResult<Boolean> {
        favoriteCalls++
        if (failMutation) {
            return NetworkResult.Failure(NetworkError.Network(IllegalStateException()))
        }
        return NetworkResult.Success(favoriteGate?.await() ?: favorite)
    }
    override suspend fun loan(bookId: Long): NetworkResult<Loan> {
        loanCalls++
        loanGate?.await()
        if (failMutation) {
            return NetworkResult.Failure(NetworkError.Network(IllegalStateException()))
        }
        return NetworkResult.Success(Loan(1, bookId, "2026-10-20", "PENDING", "책"))
    }
    override suspend fun books(page: Int, size: Int, sort: BookSort, category: String?) = error("unused")
    override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int) = error("unused")
    override suspend fun newBooks(page: Int, size: Int) = error("unused")
    override suspend fun categories() = error("unused")
    override suspend fun todayRecommendations() = error("unused")
    override suspend fun purchaseLinks(bookId: Long) = error("unused")
    override suspend fun extendLoan(loanId: Long) = error("unused")
}
