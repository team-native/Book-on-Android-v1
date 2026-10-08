package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.BookDetail
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.GetBookDetailUseCase
import com.teamnative.bookon.feature.book.domain.ToggleFavoriteUseCase
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
    fun `successful detail ends initial loading`() =
        runTest {
            val repository = RecoveryBookRepository()
            val viewModel = detail(repository)
            viewModel.load(1L)
            runCurrent()
            assertFalse(viewModel.state.value.isInitialLoading)
            assertEquals(
                "book 1",
                viewModel.state.value.content
                    ?.title,
            )
        }

    @Test
    fun `obsolete detail cannot replace another book`() =
        runTest {
            val old = CompletableDeferred<NetworkResult<BookDetail>>()
            val repository =
                RecoveryBookRepository().apply {
                    onDetail = { id ->

                        if (id == 1L) {
                            withContext(NonCancellable) {
                                old.await()
                            }
                        } else {
                            detailResponse(id)
                        }
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
                viewModel.state.value.content
                    ?.title,
            )
        }

    @Test
    fun `duplicate favorite and refresh during favorite are blocked`() =
        runTest {
            val pending = CompletableDeferred<NetworkResult<Boolean>>()
            var favorites = 0
            var detailCalls = 0
            val repository =
                RecoveryBookRepository().apply {
                    onDetail = { id ->
                        detailCalls++
                        detailResponse(id)
                    }
                    onFavorite = { _, _ ->
                        favorites++
                        pending.await()
                    }
                }
            val viewModel = detail(repository)
            viewModel.load(1L)
            runCurrent()
            viewModel.toggleFavorite()
            runCurrent()
            viewModel.toggleFavorite()
            viewModel.load(1L, forceRefresh = true)
            runCurrent()
            assertEquals(1, favorites)
            assertEquals(1, detailCalls)
            pending.complete(NetworkResult.Success(true))
            runCurrent()
            assertTrue(
                viewModel.state.value.content
                    ?.isFavorite == true,
            )
            assertFalse(
                viewModel.state.value.content
                    ?.isFavoriteSubmitting == true,
            )
        }

    @Test
    fun `failed detail refresh keeps content and error until successful retry`() =
        runTest {
            var detailCalls = 0
            var detailFails = true
            val repository =
                RecoveryBookRepository().apply {
                    onDetail = { id ->
                        detailCalls++
                        if (detailCalls > 1 && detailFails) failure() else detailResponse(id)
                    }
                }
            val viewModel = detail(repository)
            viewModel.load(1L)
            runCurrent()
            viewModel.load(1L, forceRefresh = true)
            runCurrent()
            assertEquals(
                "book 1",
                viewModel.state.value.content
                    ?.title,
            )
            assertFalse(viewModel.state.value.isRefreshing)
            assertNotNull(viewModel.state.value.errorMessage)
            viewModel.toggleFavorite()
            runCurrent()
            assertNotNull(viewModel.state.value.errorMessage)
            assertFalse(
                viewModel.state.value.content
                    ?.isFavoriteSubmitting == true,
            )
            detailFails = false
            viewModel.load(1L, forceRefresh = true)
            runCurrent()
            assertNull(viewModel.state.value.errorMessage)
            assertEquals(3, detailCalls)
        }

    private fun detail(repository: BookRepository) =
        BookOnBookDetailViewModel(
            GetBookDetailUseCase(repository),
            ToggleFavoriteUseCase(repository),
        )
}
