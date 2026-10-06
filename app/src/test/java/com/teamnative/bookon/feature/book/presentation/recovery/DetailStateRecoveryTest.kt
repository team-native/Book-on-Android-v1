package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.*
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailViewModel
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
class DetailStateRecoveryTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() {
        Dispatchers.setMain(dispatcher)
    }
    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful detail ends initial loading`() = runTest {
        val repository = RecoveryBookRepository()
        val viewModel = detail(repository)
        viewModel.load(1L)
        runCurrent()
        assertFalse(viewModel.state.value.isInitialLoading)
        assertEquals(
            "book 1",
            viewModel.state.value.content?.title
        )
    }

    @Test
    fun `obsolete detail cannot replace another book`() = runTest {
        val old = CompletableDeferred<NetworkResult<BookDetail>>()
        val repository = RecoveryBookRepository().apply {
            onDetail = { id ->

                if (id == 1L) withContext(NonCancellable) {
                    old.await()
                } else detailResponse(id)
            }
        }
        val viewModel = detail(repository)
        viewModel.load(1L)
        runCurrent()
        viewModel.load(2L)
        runCurrent()
        old.complete(detailResponse(1L))
        runCurrent()
        assertEquals(
            "book 2",
            viewModel.state.value.content?.title
        )
    }

    @Test
    fun `duplicate favorite and loan during favorite are blocked`() = runTest {
        val pending = CompletableDeferred<NetworkResult<Boolean>>()
        var favorites = 0
        var loans = 0
        val repository = RecoveryBookRepository().apply {
            onFavorite = { _, _ ->

                favorites++
                pending.await()
            }
            onLoan = { id ->

                loans++
                NetworkResult.Success(Loan(
                        1L,
                        id,
                        "date",
                        "BORROWED",
                        "book"
                ))
            }
        }
        val viewModel = detail(repository)
        viewModel.load(1L)
        runCurrent()
        viewModel.toggleFavorite()
        runCurrent()
        viewModel.toggleFavorite()
        viewModel.loan()
        runCurrent()
        assertEquals(
            1,
            favorites
        )
        assertEquals(
            0,
            loans
        )
        pending.complete(NetworkResult.Success(true))
        runCurrent()
        assertTrue(viewModel.state.value.content?.isFavorite == true)
        assertFalse(viewModel.state.value.content?.isFavoriteSubmitting == true)
    }

    @Test
    fun `loan success and failed detail refresh require GET retry before another POST`() = runTest {
        var detailCalls = 0
        var loanCalls = 0
        var detailFails = true
        val repository = RecoveryBookRepository().apply {
            onDetail = { id ->

                detailCalls++
                if (detailCalls > 1 && detailFails) failure() else detailResponse(id)
            }
            onLoan = { id ->

                loanCalls++
                NetworkResult.Success(Loan(
                        1L,
                        id,
                        "date",
                        "BORROWED",
                        "book"
                ))
            }
        }
        val viewModel = detail(repository)
        viewModel.load(1L)
        runCurrent()
        viewModel.loan()
        runCurrent()
        assertTrue(viewModel.state.value.isLoanStateUnconfirmed)
        assertNotNull(viewModel.state.value.errorMessage)
        viewModel.toggleFavorite()
        runCurrent()
        assertTrue(viewModel.state.value.isLoanStateUnconfirmed)
        assertNotNull(viewModel.state.value.errorMessage)
        viewModel.loan()
        runCurrent()
        assertEquals(
            1,
            loanCalls
        )
        detailFails = false
        viewModel.load(
            1L,
            forceRefresh = true
        )
        runCurrent()
        assertFalse(viewModel.state.value.isLoanStateUnconfirmed)
        assertEquals(
            3,
            detailCalls
        )
    }

    private fun detail(repository: BookRepository) = BookOnBookDetailViewModel(
        GetBookDetailUseCase(repository),
        RequestLoanUseCase(repository),
        ToggleFavoriteUseCase(repository),
    )

}
