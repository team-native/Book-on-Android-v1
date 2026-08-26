package com.teamnative.bookon.feature.my.data

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.my.domain.MyProfile
import com.teamnative.bookon.feature.my.domain.MyLoan
import com.teamnative.bookon.feature.my.domain.MyLoanPage
import com.teamnative.bookon.feature.my.domain.FavoriteBook
import com.teamnative.bookon.feature.my.domain.FavoriteBookPage
import com.teamnative.bookon.feature.my.domain.MyRepository
import com.teamnative.bookon.feature.my.domain.NotificationSettings
import com.teamnative.bookon.feature.my.domain.AccountDeletion
import com.teamnative.bookon.feature.my.domain.MyUser
import com.teamnative.bookon.feature.my.domain.ProfileImage
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(private val remote: MyRemoteDataSource) : MyRepository {
    /** 마이페이지 서버 응답을 화면에서 사용할 사용자·대출 요약 도메인 모델로 변환한다. */
    override suspend fun profile(): NetworkResult<MyProfile> = remote.me().map { myPage ->
        MyProfile(
            name = myPage.user.name,
            department = myPage.user.department,
            profileImageUrl = myPage.user.profileImageUrl,
            currentLoanCount = myPage.loanSummary.currentLoanCount,
            overdueCount = myPage.loanSummary.overdueCount,
            totalLoanCount = myPage.loanSummary.totalLoanCount,
            notificationSettings = NotificationSettings(
                dueDateReminder = myPage.notificationSettings.dueDateReminder,
                newBookReminder = myPage.notificationSettings.newBookReminder,
            ),
        )
    }
    override suspend fun updateNotificationSettings(dueDateReminder: Boolean, newBookReminder: Boolean): NetworkResult<NotificationSettings> = remote.updateNotificationSettings(dueDateReminder, newBookReminder).map { NotificationSettings(it.dueDateReminder, it.newBookReminder) }
    override suspend fun updateProfile(
        name: String?,
        department: String?,
        grade: Int?,
        classNo: Int?,
        studentNo: String?,
    ): NetworkResult<MyUser> = remote.updateProfile(UpdateMyProfileRequestDto(name, department, grade, classNo, studentNo)).map {
        it.user.toDomain()
    }
    override suspend fun requestAccountDeletion(reason: String?): NetworkResult<AccountDeletion> = remote.requestAccountDeletion(reason).map {
        AccountDeletion(it.requestId, it.status, it.requestedAt)
    }
    override suspend fun uploadProfileImage(contentType: String, imageBytes: ByteArray): NetworkResult<ProfileImage> = remote.uploadProfileImage(contentType, imageBytes).map {
        ProfileImage(it.profileImageUrl)
    }
    override suspend fun deleteProfileImage(): NetworkResult<ProfileImage> = remote.deleteProfileImage().map {
        ProfileImage(it.profileImageUrl)
    }
    override suspend fun currentLoans(): NetworkResult<List<MyLoan>> = remote.currentLoans().map { loans -> loans.items.map { it.toDomain() } }
    override suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<MyLoanPage> = remote.loanHistory(page, size, status).map { loans -> MyLoanPage(loans.items.map { it.toDomain() }, loans.pagination?.page ?: page, loans.pagination?.hasNext ?: false) }
    override suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPage> = remote.favorites(page, size).map { books -> FavoriteBookPage(books.items.map { FavoriteBook(it.bookId, it.title, it.author, it.libraryNumber, it.loanAvailable) }, books.pagination?.page ?: page, books.pagination?.hasNext ?: false) }
}
private fun UserDto.toDomain() = MyUser(
    userId = userId,
    email = email,
    name = name,
    department = department,
    gender = gender,
    grade = grade,
    classNo = classNo,
    studentNo = studentNo,
    profileImageUrl = profileImageUrl,
)
private fun LoanHistoryDto.toDomain() = MyLoan(loanId, bookId, title, dueDate, dDay, status)
private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> = when (this) {
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Failure -> this
}
