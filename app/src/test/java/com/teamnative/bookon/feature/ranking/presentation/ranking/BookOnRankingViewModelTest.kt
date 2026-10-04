package com.teamnative.bookon.feature.ranking.presentation.ranking

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.ranking.domain.GetReaderRankingUseCase
import com.teamnative.bookon.feature.ranking.domain.RankingRepository
import com.teamnative.bookon.feature.ranking.domain.Reader
import com.teamnative.bookon.feature.ranking.domain.ReaderRanking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnRankingViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successfulRanking_handlesZeroThroughFourReaders() = runTest {
        for (count in 0..4) {
            val repository = object : RankingRepository {
                override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> {
                    return NetworkResult.Success(
                        ReaderRanking(year, "annual", (1..count).map { rank ->
                            Reader(rank, "Reader $rank", "AI", rank)
                        }),
                    )
                }
            }
            val viewModel = BookOnRankingViewModel(GetReaderRankingUseCase(repository))
            runCurrent()
            val uiState = viewModel.uiState.value
            assertEquals(count == 0, uiState.isEmpty)
            assertEquals((count - 3).coerceAtLeast(0), uiState.list.members.size)
            assertEquals(if (count == 0) 0 else 1, uiState.podium.first.rank)
            assertFalse(uiState.isInitialLoading)
        }
    }

    @Test
    fun olderRankingResponseDoesNotOverwriteRetry() = runTest {
        val stale = kotlinx.coroutines.CompletableDeferred<NetworkResult<ReaderRanking>>()
        var requests = 0
        val repository = object : RankingRepository {
            override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> {
                requests++
                if (requests == 1) {
                    return kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) { stale.await() }
                }
                return NetworkResult.Success(ReaderRanking(year, "annual", listOf(Reader(1, "Latest", "AI", 3))))
            }
        }
        val viewModel = BookOnRankingViewModel(GetReaderRankingUseCase(repository))
        runCurrent()
        viewModel.retry()
        runCurrent()
        stale.complete(NetworkResult.Failure(NetworkError.Network(java.io.IOException("late offline"))))
        runCurrent()
        assertEquals("Latest", viewModel.uiState.value.podium.first.name)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun retryFailureKeepsPreviouslyLoadedReaders() = runTest {
        var requests = 0
        val repository = object : RankingRepository {
            override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> {
                requests++
                if (requests == 1) {
                    return NetworkResult.Success(ReaderRanking(year, "annual", listOf(Reader(1, "Reader", "AI", 3))))
                }
                return NetworkResult.Failure(NetworkError.Network(java.io.IOException("offline")))
            }
        }
        val viewModel = BookOnRankingViewModel(GetReaderRankingUseCase(repository))
        runCurrent()
        viewModel.retry()
        runCurrent()
        assertEquals("Reader", viewModel.uiState.value.podium.first.name)
        assertNotNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isEmpty)
    }

    @Test
    fun firstFailureCanRetryToSuccessfulEmptyRanking() = runTest {
        var requests = 0
        val repository = object : RankingRepository {
            override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> {
                requests++
                return if (requests == 1) {
                    NetworkResult.Failure(NetworkError.Network(java.io.IOException("offline")))
                } else {
                    NetworkResult.Success(ReaderRanking(year, "annual", emptyList()))
                }
            }
        }
        val viewModel = BookOnRankingViewModel(GetReaderRankingUseCase(repository))
        runCurrent()
        assertNotNull(viewModel.uiState.value.errorMessage)
        viewModel.retry()
        runCurrent()
        assertTrue(viewModel.uiState.value.isEmpty)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun oldSuccessCannotReplaceNewerSuccess() = runTest {
        val stale = kotlinx.coroutines.CompletableDeferred<NetworkResult<ReaderRanking>>()
        var requests = 0
        val repository = object : RankingRepository {
            override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> {
                requests++
                if (requests == 1) {
                    return kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) { stale.await() }
                }
                return NetworkResult.Success(ReaderRanking(year, "annual", listOf(Reader(1, "Latest", "AI", 3))))
            }
        }
        val viewModel = BookOnRankingViewModel(GetReaderRankingUseCase(repository))
        runCurrent()
        viewModel.retry()
        runCurrent()
        stale.complete(NetworkResult.Success(ReaderRanking(2025, "old", listOf(Reader(1, "Old", "AI", 9)))))
        runCurrent()
        assertEquals("Latest", viewModel.uiState.value.podium.first.name)
    }

    @Test
    fun failureDoesNotClaimThatRankingIsEmpty() = runTest {
        val repository = object : RankingRepository {
            override suspend fun readers(year: Int, limit: Int): NetworkResult<ReaderRanking> {
                return NetworkResult.Failure(NetworkError.Network(java.io.IOException("offline")))
            }
        }
        val viewModel = BookOnRankingViewModel(GetReaderRankingUseCase(repository))
        runCurrent()
        assertFalse(viewModel.uiState.value.isEmpty)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }
}
