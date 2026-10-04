package com.teamnative.bookon.feature.notification.presentation

import com.teamnative.bookon.core.network.*
import com.teamnative.bookon.feature.notification.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnNotificationsViewModelTest {
    @Test
    fun `read failure retains body and unread retry confirms server`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val repository = FakeNotifications()
            val viewModel = createViewModel(repository)
            viewModel.refresh()
            repository.failRead = true
            viewModel.expand(7)
            assertFalse(viewModel.uiState.value.notifications.single().isRead)
            assertEquals("body", viewModel.uiState.value.notifications.single().body)
            assertTrue(viewModel.uiState.value.hasError)
            repository.failRead = false
            viewModel.retryRead(7)
            assertEquals(7L, viewModel.uiState.value.expandedId)
            assertTrue(viewModel.uiState.value.notifications.single().isRead)
            assertEquals(0, viewModel.uiState.value.unreadCount)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `failed next page retries same page without erasing first`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val repository = FakeNotifications()
            val viewModel = createViewModel(repository)
            viewModel.refresh()
            repository.failPage = true
            viewModel.loadNextPage()
            assertEquals(1, viewModel.uiState.value.notifications.size)
            repository.failPage = false
            viewModel.retry()
            assertEquals(listOf(1, 2, 2), repository.pages)
            assertEquals(2, viewModel.uiState.value.notifications.size)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `read all accepts false and rechecks server`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val repository = FakeNotifications()
            val viewModel = createViewModel(repository)
            viewModel.refresh()
            viewModel.readAll()
            assertEquals(listOf(1, 1), repository.pages)
            assertEquals(0, viewModel.uiState.value.unreadCount)
            assertTrue(viewModel.uiState.value.notifications.single().isRead)
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun createViewModel(repository: NotificationRepository) = BookOnNotificationsViewModel(
        GetNotificationsUseCase(repository), MarkNotificationReadUseCase(repository),
        MarkAllNotificationsReadUseCase(repository), GetUnreadNotificationCountUseCase(repository),
    )
}

private class FakeNotifications : NotificationRepository {
    var failRead = false
    var failPage = false
    var isRead = false
    val pages = mutableListOf<Int>()
    private val failure = NetworkResult.Failure(NetworkError.Network(IllegalStateException("offline")))
    override suspend fun notifications(page: Int, size: Int): NetworkResult<NotificationPage> {
        pages += page
        if (page == 2 && failPage) {
            return failure
        }
        return NetworkResult.Success(NotificationPage(
            listOf(BookOnNotification(if (page == 1) 7 else 8, NotificationKind.Notice, "title", "body", isRead, "date", null)),
            page, page == 1, 2,
        ))
    }
    override suspend fun unreadCount() = NetworkResult.Success(if (isRead) 0 else 1)
    override suspend fun markRead(notificationId: Long): NetworkResult<NotificationRead> {
        if (failRead) {
            return failure
        }
        isRead = true
        return NetworkResult.Success(NotificationRead(notificationId, true))
    }
    override suspend fun markAllRead(): NetworkResult<Boolean> {
        isRead = true
        return NetworkResult.Success(false)
    }
}
