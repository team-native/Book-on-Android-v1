package com.teamnative.bookon.feature.my.domain

import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke() = repository.profile()
}

class UpdateNotificationSettingsUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(
        dueDateReminder: Boolean,
        newBookReminder: Boolean,
    ) = repository.updateNotificationSettings(dueDateReminder, newBookReminder)
}

class GetCurrentLoansUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke() = repository.currentLoans()
}

class GetLoanHistoryUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(
        page: Int,
        size: Int,
        status: String,
    ) = repository.loanHistory(page, size, status)
}

class GetFavoriteBooksUseCase @Inject constructor(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int) = repository.favorites(page, size)
}
