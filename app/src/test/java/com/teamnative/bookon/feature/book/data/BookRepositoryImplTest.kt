package com.teamnative.bookon.feature.book.data

import com.teamnative.bookon.core.network.NetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BookRepositoryImplTest {
    @Test
    fun `카테고리 DTO를 도메인 모델로 변환한다`() =
        runTest {
            val repository =
                BookRepositoryImpl(
                    object : BookRemoteDataSource {
                        override suspend fun categories() =
                            NetworkResult.Success(
                                CategoryListDto(listOf(CategoryDto(7, "NOVEL", "소설", 10))),
                            )

                        override suspend fun books(
                            page: Int,
                            size: Int,
                            sort: String,
                            category: String?,
                        ) = error("not used")

                        override suspend fun search(
                            keyword: String?,
                            libraryNumber: String?,
                            page: Int,
                            size: Int,
                        ) = error("not used")

                        override suspend fun newBooks(
                            page: Int,
                            size: Int,
                        ) = error("not used")

                        override suspend fun todayRecommendations() = error("not used")

                        override suspend fun purchaseLinks(bookId: Long) = error("not used")

                        override suspend fun book(bookId: Long) = error("not used")

                        override suspend fun favorite(
                            bookId: Long,
                            favorite: Boolean,
                        ) = error("not used")

                        override suspend fun loan(bookId: Long) = error("not used")

                        override suspend fun extendLoan(loanId: Long) = error("not used")
                    },
                )

            val result = repository.categories() as NetworkResult.Success

            assertEquals(7L, result.data.single().categoryId)
            assertEquals(listOf("NOVEL" to "소설"), result.data.map { it.code to it.name })
            assertEquals(10, result.data.single().bookCount)
        }

    @Test
    fun `상세 수량과 nullable 상태를 매핑하고 표지 우선순위를 유지한다`() =
        runTest {
            val remote =
                object : BookRemoteDataSource {
                    override suspend fun book(bookId: Long) =
                        NetworkResult.Success(
                            BookDetailDto(
                                bookId = bookId,
                                title = "책",
                                author = "저자",
                                publisher = "출판사",
                                category = "분류",
                                libraryNumber = "813",
                                loanAvailable = false,
                                status = null,
                                coverUrl = "https://example.com/primary.jpg",
                                coverImageUrl = "https://example.com/fallback.jpg",
                                totalQuantity = 3,
                                availableQuantity = 0,
                            ),
                        )

                    override suspend fun categories() = error("unused")

                    override suspend fun books(
                        page: Int,
                        size: Int,
                        sort: String,
                        category: String?,
                    ) = error("unused")

                    override suspend fun search(
                        keyword: String?,
                        libraryNumber: String?,
                        page: Int,
                        size: Int,
                    ) = error("unused")

                    override suspend fun newBooks(
                        page: Int,
                        size: Int,
                    ) = error("unused")

                    override suspend fun todayRecommendations() = error("unused")

                    override suspend fun purchaseLinks(bookId: Long) = error("unused")

                    override suspend fun favorite(
                        bookId: Long,
                        favorite: Boolean,
                    ) = error("unused")

                    override suspend fun loan(bookId: Long) = error("unused")

                    override suspend fun extendLoan(loanId: Long) = error("unused")
                }
            val response = BookRepositoryImpl(remote).book(8013595087L) as NetworkResult.Success
            assertEquals(8013595087L, response.data.book.id)
            assertEquals("", response.data.book.status)
            assertEquals("https://example.com/primary.jpg", response.data.book.coverImageUrl)
            assertEquals(3, response.data.totalQuantity)
            assertEquals(0, response.data.availableQuantity)
        }
}
