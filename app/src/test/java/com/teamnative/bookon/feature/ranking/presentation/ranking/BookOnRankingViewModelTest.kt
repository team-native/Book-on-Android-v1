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
