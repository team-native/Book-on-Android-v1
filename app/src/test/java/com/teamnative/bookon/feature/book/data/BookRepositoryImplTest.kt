package com.teamnative.bookon.feature.book.data

import com.teamnative.bookon.core.network.NetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BookRepositoryImplTest {
    @Test
    fun `카테고리 DTO를 도메인 모델로 변환한다`() = runTest {
        val repository = BookRepositoryImpl(
            object : BookRemoteDataSource {
                override suspend fun categories() = NetworkResult.Success(
                    CategoryListDto(listOf(CategoryDto(7, "NOVEL", "소설", 10))),
                )

                override suspend fun books(page: Int, size: Int, sort: String, category: String?) = error("not used")
                override suspend fun search(keyword: String?, libraryNumber: String?, page: Int, size: Int) = error("not used")
                override suspend fun newBooks(page: Int, size: Int) = error("not used")
                override suspend fun todayRecommendations() = error("not used")
                override suspend fun purchaseLinks(bookId: Long) = error("not used")
                override suspend fun book(bookId: Long) = error("not used")
                override suspend fun favorite(bookId: Long, favorite: Boolean) = error("not used")
                override suspend fun loan(bookId: Long) = error("not used")
                override suspend fun extendLoan(loanId: Long) = error("not used")
            },
        )

        val result = repository.categories() as NetworkResult.Success

        assertEquals(7L, result.data.single().categoryId)
        assertEquals(listOf("NOVEL" to "소설"), result.data.map { it.code to it.name })
        assertEquals(10, result.data.single().bookCount)
    }
}
