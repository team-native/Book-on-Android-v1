package com.teamnative.bookon.feature.book.presentation.recovery

import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.my.domain.AccountDeletion
import com.teamnative.bookon.feature.my.domain.FavoriteBookPage
import com.teamnative.bookon.feature.my.domain.MyLoan
import com.teamnative.bookon.feature.my.domain.MyLoanPage
import com.teamnative.bookon.feature.my.domain.MyProfile
import com.teamnative.bookon.feature.my.domain.MyRepository
import com.teamnative.bookon.feature.my.domain.MyUser
import com.teamnative.bookon.feature.my.domain.NotificationSettings
import com.teamnative.bookon.feature.my.domain.ProfileImage

internal class RecoveryMyRepository : MyRepository {
    var onCurrentLoans: suspend () -> NetworkResult<List<MyLoan>> = {
        NetworkResult.Success(emptyList())
    }
    var onHistory: suspend (Int) -> NetworkResult<MyLoanPage> = {
        NetworkResult.Success(
            MyLoanPage(
                emptyList(),
                it,
                false,
            ),
        )
    }
    var onFavorites: suspend (Int) -> NetworkResult<FavoriteBookPage> = {
        NetworkResult.Success(
            FavoriteBookPage(
                emptyList(),
                it,
                false,
            ),
        )
    }

    override suspend fun profile(): NetworkResult<MyProfile> = error("not used")

    override suspend fun updateNotificationSettings(
        dueDateReminder: Boolean,
        newBookReminder: Boolean,
        noticeReminder: Boolean,
    ): NetworkResult<NotificationSettings> = error("not used")

    override suspend fun updateProfile(
        name: String?,
        department: String?,
        grade: Int?,
        classNo: Int?,
        studentNo: String?,
    ): NetworkResult<MyUser> = error("not used")

    override suspend fun requestAccountDeletion(reason: String?): NetworkResult<AccountDeletion> = error("not used")

    override suspend fun uploadProfileImage(
        contentType: String,
        imageBytes: ByteArray,
    ): NetworkResult<ProfileImage> = error("not used")

    override suspend fun deleteProfileImage(): NetworkResult<ProfileImage> = error("not used")

    override suspend fun currentLoans() = onCurrentLoans()

    override suspend fun loanHistory(
        page: Int,
        size: Int,
        status: String,
    ) = onHistory(page)

    override suspend fun favorites(
        page: Int,
        size: Int,
    ) = onFavorites(page)
}
