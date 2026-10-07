package com.teamnative.bookon.feature.home.presentation.home

import com.teamnative.bookon.core.network.NetworkError
import com.teamnative.bookon.core.network.NetworkResult
import com.teamnative.bookon.feature.book.domain.Book
import com.teamnative.bookon.feature.book.domain.BookCategory
import com.teamnative.bookon.feature.book.domain.BookDetail
import com.teamnative.bookon.feature.book.domain.BookPage
import com.teamnative.bookon.feature.book.domain.BookRepository
import com.teamnative.bookon.feature.book.domain.BookSort
import com.teamnative.bookon.feature.book.domain.GetBooksUseCase
import com.teamnative.bookon.feature.book.domain.Loan
import com.teamnative.bookon.feature.book.domain.LoanExtension
import com.teamnative.bookon.feature.book.domain.PurchaseLink
import com.teamnative.bookon.feature.book.domain.TodayRecommendation
import com.teamnative.bookon.feature.home.domain.GetHomeUseCase
import com.teamnative.bookon.feature.home.domain.HomeNoticePage
import com.teamnative.bookon.feature.home.domain.HomeRecommendation
import com.teamnative.bookon.feature.home.domain.GetNoticesUseCase
import com.teamnative.bookon.feature.home.domain.HomeData
import com.teamnative.bookon.feature.home.domain.HomeRepository
import com.teamnative.bookon.feature.my.domain.AccountDeletion
import com.teamnative.bookon.feature.my.domain.FavoriteBookPage
import com.teamnative.bookon.feature.my.domain.GetMyProfileUseCase
import com.teamnative.bookon.feature.my.domain.MyLoan
import com.teamnative.bookon.feature.my.domain.MyLoanPage
import com.teamnative.bookon.feature.my.domain.MyProfile
import com.teamnative.bookon.feature.my.domain.MyRepository
import com.teamnative.bookon.feature.my.domain.MyUser
import com.teamnative.bookon.feature.my.domain.NotificationSettings
import com.teamnative.bookon.feature.my.domain.ProfileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookOnHomeViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `홈 추천 도서와 사유를 기존 홈 계약대로 화면에 반영한다`() = runTest {
        val viewModel = createViewModel(
            HomeBookRepository(),
            NetworkResult.Success(HomeData(recommendation(1L, "학교 대출 통계 기반 추천"))),
        )
        advanceUntilIdle()

        assertEquals(listOf(1L), viewModel.uiState.value.aiRecommendedBooks.map { it.id })
        assertEquals("학교 대출 통계 기반 추천", viewModel.uiState.value.aiRecommendationDescription)
        assertEquals(listOf(101L), viewModel.uiState.value.popularBooks.map { it.id })
    }

    @Test
    fun `추천 조회가 실패해도 성공한 인기 책은 유지하고 오류를 표시한다`() = runTest {
        val viewModel = createViewModel(
            HomeBookRepository(),
            NetworkResult.Failure(NetworkError.Network(IllegalStateException("network"))),
        )
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.aiRecommendedBooks.isEmpty())
        assertEquals(listOf(101L), viewModel.uiState.value.popularBooks.map { book -> book.id })
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    /** Home에서 사용하는 UseCase에 테스트 Repository를 연결해 ViewModel을 만든다. */
    private fun createViewModel(
        bookRepository: BookRepository,
        homeResult: NetworkResult<HomeData>,
    ): BookOnHomeViewModel {
        return BookOnHomeViewModel(
            getHome = GetHomeUseCase(HomeNoticeRepository(homeResult)),
            getNotices = GetNoticesUseCase(HomeNoticeRepository(homeResult)),
            getBooks = GetBooksUseCase(bookRepository),
            getMyProfile = GetMyProfileUseCase(HomeProfileRepository()),
        )
    }

    private fun recommendation(
        bookId: Long,
        reason: String?,
    ) = HomeRecommendation(
        bookId = bookId,
        title = "추천 도서 $bookId",
        author = "추천 작가",
        coverImageUrl = null,
        reason = reason,
    )
}

private class HomeBookRepository(
    private val recommendationsResult: NetworkResult<List<TodayRecommendation>> = NetworkResult.Success(emptyList()),
) : BookRepository {
    override suspend fun books(
        page: Int,
        size: Int,
        sort: BookSort,
        category: String?,
    ): NetworkResult<BookPage> {
        return NetworkResult.Success(
            BookPage(
                items = listOf(createBook(id = 101L)),
                page = page,
                hasNext = false,
                totalCount = 1,
            ),
        )
    }

    override suspend fun newBooks(
        page: Int,
        size: Int,
    ): NetworkResult<BookPage> {
        return NetworkResult.Success(
            BookPage(
                items = listOf(createBook(id = 201L)),
                page = page,
                hasNext = false,
                totalCount = 1,
            ),
        )
    }

    override suspend fun todayRecommendations(): NetworkResult<List<TodayRecommendation>> {
        return recommendationsResult
    }

    override suspend fun search(
        keyword: String?,
        libraryNumber: String?,
        page: Int,
        size: Int,
    ): NetworkResult<BookPage> = error("not used")

    override suspend fun categories(): NetworkResult<List<BookCategory>> = error("not used")
    override suspend fun purchaseLinks(bookId: Long): NetworkResult<List<PurchaseLink>> = error("not used")
    override suspend fun book(bookId: Long): NetworkResult<BookDetail> = error("not used")
    override suspend fun favorite(bookId: Long, favorite: Boolean): NetworkResult<Boolean> = error("not used")
    override suspend fun loan(bookId: Long): NetworkResult<Loan> = error("not used")
    override suspend fun extendLoan(loanId: Long): NetworkResult<LoanExtension> = error("not used")

    private fun createBook(id: Long) = Book(
        id = id,
        title = "도서 $id",
        author = "작가",
        publisher = "출판사",
        category = "총류",
        libraryNumber = "004",
        coverImageUrl = null,
        loanAvailable = true,
        status = "대출가능",
    )
}

private class HomeNoticeRepository(
    private val homeResult: NetworkResult<HomeData>,
) : HomeRepository {
    override suspend fun home(limit: Int): NetworkResult<HomeData> = homeResult

    override suspend fun notices(
        page: Int,
        size: Int,
    ): NetworkResult<HomeNoticePage> = NetworkResult.Success(HomeNoticePage(emptyList(), page, false, 0))
}

private class HomeProfileRepository : MyRepository {
    override suspend fun profile(): NetworkResult<MyProfile> {
        return NetworkResult.Success(
            MyProfile(
                name = "홍길동",
                department = "소프트웨어 개발과",
                profileImageUrl = null,
                currentLoanCount = 0,
                overdueCount = 0,
                totalLoanCount = 0,
                notificationSettings = NotificationSettings(
                    dueDateReminder = false,
                    newBookReminder = false,
                    noticeReminder = false,
                ),
            ),
        )
    }

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
    override suspend fun uploadProfileImage(contentType: String, imageBytes: ByteArray): NetworkResult<ProfileImage> = error("not used")
    override suspend fun deleteProfileImage(): NetworkResult<ProfileImage> = error("not used")
    override suspend fun currentLoans(): NetworkResult<List<MyLoan>> = error("not used")
    override suspend fun loanHistory(page: Int, size: Int, status: String): NetworkResult<MyLoanPage> = error("not used")
    override suspend fun favorites(page: Int, size: Int): NetworkResult<FavoriteBookPage> = error("not used")
}
