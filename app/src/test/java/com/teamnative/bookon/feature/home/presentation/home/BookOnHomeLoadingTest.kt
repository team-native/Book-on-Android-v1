package com.teamnative.bookon.feature.home.presentation.home

import com.teamnative.bookon.core.network.*
import com.teamnative.bookon.feature.book.domain.*
import com.teamnative.bookon.feature.book.presentation.recovery.*
import com.teamnative.bookon.feature.home.domain.*
import com.teamnative.bookon.feature.my.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnHomeLoadingTest {
    @Test
    fun `slow profile does not hold notice and popular books hostage`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val profileGate = CompletableDeferred<NetworkResult<MyProfile>>()
            val newBooksGate = CompletableDeferred<NetworkResult<BookPage>>()
            var newBooksCalls = 0
            val books = RecoveryBookRepository().apply {
                onNewBooks = {
                    newBooksCalls++
                    newBooksGate.await()
                }
            }
            val my = object : MyRepository by RecoveryMyRepository() {
                override suspend fun profile() = profileGate.await()
            }
            val home = object : HomeRepository {
                override suspend fun home(limit: Int) = NetworkResult.Success(HomeData(null))
                override suspend fun notices(page: Int, size: Int) = NetworkResult.Success(
                    HomeNoticePage(listOf(HomeNotice(7, "notice", "summary", "date")), 1, false, 1),
                )
            }
            val vm = BookOnHomeViewModel(GetHomeUseCase(home), GetNoticesUseCase(home), GetBooksUseCase(books), GetMyProfileUseCase(my))
            try {
                assertFalse(vm.uiState.value.isInitialLoading)
                assertEquals("notice", vm.uiState.value.notice?.title)
                assertEquals(1, vm.uiState.value.popularBooks.size)
                assertEquals(0, newBooksCalls)
            } finally {
                profileGate.complete(NetworkResult.Failure(NetworkError.Network(IllegalStateException("offline"))))
                newBooksGate.complete(NetworkResult.Success(BookPage(emptyList(), 1, false, 0)))
                assertTrue(vm.uiState.value.loadingSections.isEmpty())
                assertTrue(vm.uiState.value.sectionErrors.containsKey(HomeSection.Profile))
            }
        } finally {
            Dispatchers.resetMain()
        }
    }
    @Test
    fun `cancelled older home response cannot overwrite retry`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val older = CompletableDeferred<NetworkResult<HomeData>>()
            var calls = 0
            val home = object : HomeRepository {
                override suspend fun home(limit: Int): NetworkResult<HomeData> {
                    calls++
                    return if (calls == 1) {
                        withContext(NonCancellable) { older.await() }
                    } else {
                        NetworkResult.Success(HomeData(HomeRecommendation(2, "new", "author", null, "reason")))
                    }
                }
                override suspend fun notices(page: Int, size: Int) = NetworkResult.Success(HomeNoticePage(emptyList(), 1, false, 0))
            }
            val my = object : MyRepository by RecoveryMyRepository() {
                override suspend fun profile() = NetworkResult.Failure(NetworkError.Network(IllegalStateException("offline")))
            }
            val vm = BookOnHomeViewModel(GetHomeUseCase(home), GetNoticesUseCase(home), GetBooksUseCase(RecoveryBookRepository()), GetMyProfileUseCase(my))
            vm.load()
            assertEquals("new", vm.uiState.value.aiRecommendedBooks.single().title)
            older.complete(NetworkResult.Success(HomeData(HomeRecommendation(1, "old", "author", null, "reason"))))
            runCurrent()
            assertEquals("new", vm.uiState.value.aiRecommendedBooks.single().title)
            assertTrue(vm.uiState.value.sectionErrors.containsKey(HomeSection.Profile))
            assertTrue(vm.uiState.value.loadingSections.isEmpty())
        } finally {
            Dispatchers.resetMain()
        }
    }
}
