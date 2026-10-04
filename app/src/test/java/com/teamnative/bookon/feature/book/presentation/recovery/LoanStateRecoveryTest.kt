package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.*
import com.teamnative.bookon.feature.my.domain.*
import com.teamnative.bookon.feature.my.presentation.loanhistory.BookOnLoanHistoryViewModel
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
class LoanStateRecoveryTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() {
        Dispatchers.setMain(dispatcher)
    }
    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `old current loans response cannot replace returned filter`() = runTest {
        val old = CompletableDeferred<NetworkResult<List<MyLoan>>>()
        val repository = RecoveryMyRepository().apply {
            onCurrentLoans = {
                withContext(NonCancellable) {
                    old.await()
                }
            }
        }
        val viewModel = loans(
            repository,
            RecoveryBookRepository()
        )
        runCurrent()
        viewModel.selectFilter(1)
        runCurrent()
        old.complete(NetworkResult.Success(listOf(loan(1L))))
        runCurrent()
        assertTrue(viewModel.uiState.value.currentLoans.isEmpty())
        assertTrue(viewModel.uiState.value.filters[1].selected)
    }

    @Test
    fun `old history page does not enter a new current loans filter`() = runTest {
        val old = CompletableDeferred<NetworkResult<MyLoanPage>>()
        val repository = RecoveryMyRepository().apply {
            onHistory = { page ->

                if (page == 2) withContext(NonCancellable) {
                    old.await()
                }
                else NetworkResult.Success(MyLoanPage(
                        listOf(loan(2L)),
                        1,
                        true
                ))
            }
        }
        val viewModel = loans(
            repository,
            RecoveryBookRepository()
        )
        runCurrent()
        viewModel.selectFilter(1)
        runCurrent()
        viewModel.loadMore()
        runCurrent()
        viewModel.selectFilter(0)
        runCurrent()
        old.complete(NetworkResult.Success(MyLoanPage(
                    listOf(loan(3L)),
                    2,
                    false
        )))
        runCurrent()
        assertTrue(viewModel.uiState.value.pastLoans.isEmpty())
        assertTrue(viewModel.uiState.value.filters[0].selected)
    }

    @Test
    fun `two extensions preserve each other and block the same loan duplicate`() = runTest {
        val first = CompletableDeferred<NetworkResult<LoanExtension>>()
        val second = CompletableDeferred<NetworkResult<LoanExtension>>()
        val repository = RecoveryMyRepository().apply {
            onCurrentLoans = {
                NetworkResult.Success(listOf(
                        loan(1L),
                        loan(2L)
                ))
            }
        }
        val requests = mutableListOf<Long>()
        val books = RecoveryBookRepository().apply {
            onExtend = { id ->

                requests += id
                if (id == 1L) first.await() else second.await()
            }
        }
        val viewModel = loans(
            repository,
            books
        )
        runCurrent()
        viewModel.extend(1L)
        viewModel.extend(2L)
        viewModel.extend(1L)
        runCurrent()
        second.complete(NetworkResult.Success(LoanExtension(
                    2L,
                    "old",
                    "second",
                    1,
                    false
        )))
        runCurrent()
        first.complete(NetworkResult.Success(LoanExtension(
                    1L,
                    "old",
                    "first",
                    1,
                    false
        )))
        runCurrent()
        assertEquals(
            listOf(
                1L,
                2L
            ),
            requests
        )
        assertEquals(
            listOf(
                "반납 예정 first",
                "반납 예정 second"
            ),
            viewModel.uiState.value.currentLoans.map {
                it.book.metaText
            }
        )
        assertTrue(viewModel.uiState.value.currentLoans.none {
                it.isExtending || it.extensionAvailable
        })
    }

    @Test
    fun `failed extension does not roll back another successful extension`() = runTest {
        val first = CompletableDeferred<NetworkResult<LoanExtension>>()
        val second = CompletableDeferred<NetworkResult<LoanExtension>>()
        val repository = RecoveryMyRepository().apply {
            onCurrentLoans = {
                NetworkResult.Success(listOf(
                        loan(1L),
                        loan(2L)
                ))
            }
        }
        val books = RecoveryBookRepository().apply {
            onExtend = { id ->

                if (id == 1L) first.await() else second.await()
            }
        }
        val viewModel = loans(
            repository,
            books
        )
        runCurrent()
        viewModel.extend(1L)
        viewModel.extend(2L)
        runCurrent()
        second.complete(NetworkResult.Success(LoanExtension(
                    2L,
                    "old",
                    "second",
                    1,
                    false
        )))
        runCurrent()
        first.complete(failure())
        runCurrent()
        assertEquals(
            "반납 예정 second",
            viewModel.uiState.value.currentLoans[1].book.metaText
        )
        assertFalse(viewModel.uiState.value.currentLoans[0].isExtending)
        assertFalse(viewModel.uiState.value.currentLoans[1].extensionAvailable)
    }

    private fun loans(
        repository: MyRepository,
        books: BookRepository
    ) = BookOnLoanHistoryViewModel(
        GetCurrentLoansUseCase(repository),
        GetLoanHistoryUseCase(repository),
        ExtendLoanUseCase(books),
    )

}

private fun loan(id: Long) = MyLoan(
    id,
    id,
    "book",
    "old",
    1,
    "BORROWED",
    true
)
