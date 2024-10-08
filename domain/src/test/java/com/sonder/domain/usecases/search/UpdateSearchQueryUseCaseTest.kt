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

class UpdateSearchQueryUseCaseTest {
    private lateinit var useCase: UpdateSearchQueryUseCase
    private val searchRepository: SearchRepository = mockk(relaxed = true)

    private fun setupTest(query: String)  {
        coEvery { searchRepository.searchQuery } returns flowOf(query)
        useCase = UpdateSearchQueryUseCase(searchRepository = searchRepository)
    }

    @Test
    fun `WHEN useCase is executed THEN it updates search query`() = runTest {
        val searchQuery = "mock"

        setupTest(searchQuery)

        useCase.execute(searchQuery)

        coVerify(exactly = 1) {
            searchRepository.updateSearchQuery(searchQuery)
        }

        assertEquals(searchQuery, searchRepository.searchQuery.first())
    }
}