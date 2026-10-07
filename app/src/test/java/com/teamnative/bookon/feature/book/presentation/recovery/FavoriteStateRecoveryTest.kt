package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.*
import com.teamnative.bookon.feature.my.domain.*
import com.teamnative.bookon.feature.my.presentation.favorites.BookOnFavoriteBooksViewModel
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
class FavoriteStateRecoveryTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() {
        Dispatchers.setMain(dispatcher)
    }
    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `late favorites page cannot revive a removed book`() = runTest {
        val page = CompletableDeferred<NetworkResult<FavoriteBookPage>>()
        val repository = RecoveryMyRepository().apply {
            onFavorites = { number ->

                if (number == 1) NetworkResult.Success(FavoriteBookPage(
                        listOf(favorite(1L)),
                        1,
                        true
                )) else page.await()
            }
        }
        val viewModel = favorites(
            repository,
            RecoveryBookRepository()
        )
        runCurrent()
        viewModel.loadNextPage()
        runCurrent()
        viewModel.removeFavorite(1L)
        runCurrent()
        page.complete(NetworkResult.Success(FavoriteBookPage(
                    listOf(
                        favorite(1L),
                        favorite(2L)
                    ),
                    2,
                    false
        )))
        runCurrent()
        assertEquals(
            listOf(2L),
            viewModel.uiState.value.books.map {
                it.id
            }
        )
    }

    @Test
    fun `favorites resume refreshes changed detail state`() = runTest {
        var hasFavorite = true
        val repository = RecoveryMyRepository().apply {
            onFavorites = {
                NetworkResult.Success(FavoriteBookPage(
                        if (hasFavorite) listOf(favorite(1L)) else emptyList(),
                        1,
                        false
                ))
            }
        }
        val viewModel = favorites(
            repository,
            RecoveryBookRepository()
        )
        runCurrent()
        viewModel.onResume()
        viewModel.markDetailOpened()
        hasFavorite = false
        viewModel.onResume()
        runCurrent()
        assertTrue(viewModel.uiState.value.books.isEmpty())
    }

    private fun favorites(
        repository: MyRepository,
        books: BookRepository
    ) = BookOnFavoriteBooksViewModel(
        GetFavoriteBooksUseCase(repository),
        ToggleFavoriteUseCase(books),
    )

}

private fun favorite(id: Long) = FavoriteBook(
    id,
    "book",
    "author",
    "number",
    true
)
