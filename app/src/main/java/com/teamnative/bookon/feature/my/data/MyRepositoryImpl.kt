package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.my.domain.MyProfile
import com.teamnative.bookon.feature.my.domain.MyLoan
import com.teamnative.bookon.feature.my.domain.MyLoanPage
import com.teamnative.bookon.feature.my.domain.FavoriteBook
import com.teamnative.bookon.feature.my.domain.FavoriteBookPage
import com.teamnative.bookon.feature.my.domain.MyRepository
import com.teamnative.bookon.feature.my.domain.NotificationSettings
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(private val remote: MyRemoteDataSource) : MyRepository {
    override suspend fun profile(): NetworkResult<MyProfile> = remote.me().map { MyProfile(it.user.name, it.user.department, it.loanSummary.currentLoanCount, it.loanSummary.overdueCount, NotificationSettings(it.notificationSettings.dueDateReminder, it.notificationSettings.newBookReminder)) }
    override suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean): NetworkResult<NotificationSettings> = remote.updateNotificationSettings(dueDateReminder, newBookReminder).map { NotificationSettings(it.dueDateReminder, it.newBookReminder) }
    override suspend fun currentLoans(): NetworkResult<List<MyLoan>> = remote.currentLoans().map { loans -> loans.items.map { it.toDomain() } }
    override suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<MyLoanPage> = remote.loanHistory(page, size, status).map { loans -> MyLoanPage(loans.items.map { it.toDomain() }, loans.pagination?.page ?: page, loans.pagination?.hasNext ?: false) }
    override suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPage> = remote.favorites(page, size).map { books -> FavoriteBookPage(books.items.map { FavoriteBook(it.bookId, it.title, it.author, it.libraryNumber, it.loanAvailable) }, books.pagination?.page ?: page, books.pagination?.hasNext ?: false) }
}
private fun LoanHistoryDto.toDomain() = MyLoan(loanId, bookId, title, dueDate, dDay, status)
private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
