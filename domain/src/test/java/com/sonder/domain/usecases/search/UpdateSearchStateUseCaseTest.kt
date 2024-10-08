package com.sonder.domain.usecases.search

import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchStateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UpdateSearchStateUseCaseTest {
    private lateinit var useCase: UpdateSearchStateUseCase
    private val searchStateRepository: SearchStateRepository = mockk(relaxed = true)

    private fun setupTest(state: SearchState)  {
        coEvery { searchStateRepository.searchState } returns flowOf(state)
        useCase = UpdateSearchStateUseCase(searchStateRepository = searchStateRepository)
    }

    @Test
    fun `WHEN useCase is executed THEN it updates search state to idle`() = runTest {
        val searchState = SearchState.Idle

        setupTest(searchState)

        useCase.execute(searchState)

        coVerify(exactly = 1) {
            searchStateRepository.updateSearchState(searchState)
        }

        assertEquals(searchState, searchStateRepository.searchState.first())
    }

    @Test
    fun `WHEN useCase is executed THEN it updates search state to searching`() = runTest {
        val searchState = SearchState.Searching

        setupTest(searchState)

        useCase.execute(searchState)

        coVerify(exactly = 1) {
            searchStateRepository.updateSearchState(searchState)
        }

        assertEquals(searchState, searchStateRepository.searchState.first())
    }
}