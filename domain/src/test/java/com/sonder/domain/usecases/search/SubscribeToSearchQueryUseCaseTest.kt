package com.sonder.domain.usecases.search

import com.sonder.domain.repositories.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SubscribeToSearchQueryUseCaseTest {
    private lateinit var useCase: SubscribeToSearchQueryUseCase
    private val searchRepository: SearchRepository = mockk(relaxed = true)

    private fun setupTest()  {
        useCase = SubscribeToSearchQueryUseCase(searchRepository = searchRepository)
    }

    @Test
    fun `WHEN useCase is executed THEN it returns search query`() = runTest {
        val searchQuery = "mock"
        coEvery { searchRepository.searchQuery } returns flowOf(searchQuery)
        setupTest()

        val expectedValue = useCase.execute(Unit)

        coVerify(exactly = 1) {
            searchRepository.searchQuery
        }

        assertEquals(searchQuery, expectedValue.first())
    }
}